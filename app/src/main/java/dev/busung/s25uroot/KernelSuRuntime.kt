package dev.busung.s25uroot

import android.content.Context

/** Runtime checks and the post-KernelSU root bridge.
 *
 * The bootstrap helper socket exists only to cross the pre-KernelSU handoff.
 * Once KernelSU has loaded, Samsung/SELinux may reject new shell/app connects to
 * that socket even though KernelSU itself is healthy. Runtime detection therefore
 * accepts three independent proofs: module visibility, an already-authorized
 * KernelSU `su` through Shizuku, or the helper's direct `--ksu-info` control probe.
 * Privileged post-load commands use KernelSU itself rather than reconnecting to
 * the temporary bootstrap socket.
 */
internal object KernelSuRuntime {
    fun isControlActive(context: Context): Boolean {
        if (NativeProbe.isKernelSuActiveSafe()) return true

        // Direct `su` probe: the vote that matters on a fresh install. KernelSU
        // authorizes the app's own `su -c id` even when no receipt, cache,
        // Shizuku pairing or helper state exists yet. Short timeout so an
        // absent/denied su never blocks the UI or boot gate.
        if (tryDirectSuProbe()) return true

        // This is the strongest userspace proof in the normal ZZI4 boot path.
        // Shizuku already runs as shell and KernelSU late-load was invoked with
        // --allow-shell, so a successful elevation proves both a live KSU control
        // channel and a usable post-root command bridge. It also avoids relying on
        // /proc/modules visibility from an untrusted app process.
        if (ShizukuController.isRunning() && ShizukuController.isGranted()) {
            val elevated = runCatching {
                ShizukuController.shell("su -c id")
            }.getOrNull()
            if (elevated != null &&
                elevated.exitCode == 0 &&
                elevated.output.contains("uid=0")
            ) {
                return true
            }
        }

        // `--ksu-info` is handled directly by the native helper and never touches
        // temp_su.sock. Keep it as a transport-independent fallback for boots where
        // Shizuku is unavailable but the app process can reach the KSU control ABI.
        val direct = runCatching {
            RootHelperShell.execute(context, "--ksu-info")
        }.getOrNull()
        return direct?.exitCode == 0
    }

    fun shizukuRootShell(command: String): LocalAdbClient.ShellResult? {
        if (!ShizukuController.isRunning() || !ShizukuController.isGranted()) return null

        val direct = runCatching { ShizukuController.shell("id") }.getOrNull()
        if (direct != null && direct.exitCode == 0 && direct.output.contains("uid=0")) {
            return runCatching { ShizukuController.shell(command) }.getOrNull()
        }

        val elevated = runCatching { ShizukuController.shell("su -c id") }.getOrNull()
        if (elevated == null || elevated.exitCode != 0 || !elevated.output.contains("uid=0")) {
            return null
        }
        return runCatching {
            ShizukuController.shell("su -c ${shellQuote(command)}")
        }.getOrNull()
    }

    private fun shellQuote(value: String): String = "'${value.replace("'", "'\\''")}'"

    /**
     * Direct `su -c id` probe from the app process. Returns true only on
     * `exit == 0` with `uid=0` in output. Fail-closed on timeout/denial:
     * absent su, SELinux denial and slow daemons all yield false quickly.
     */
    fun tryDirectSuProbe(timeoutMillis: Long = DIRECT_SU_PROBE_TIMEOUT_MILLIS): Boolean {
        val process = runCatching {
            ProcessBuilder("su", "-c", "id")
                .redirectErrorStream(true)
                .start()
        }.getOrNull() ?: return false
        return try {
            val output = StringBuilder()
            val reader = Thread({
                runCatching {
                    process.inputStream.bufferedReader().use { stream ->
                        val buffer = CharArray(1024)
                        while (true) {
                            val count = stream.read(buffer)
                            if (count <= 0) break
                            output.append(buffer, 0, count)
                            if (output.length > 4096) break
                        }
                    }
                }
            }, "rmg-direct-su-probe").apply {
                isDaemon = true
                start()
            }
            val finished = runCatching {
                process.waitFor(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
            }.getOrDefault(false)
            runCatching { reader.join(500) }
            if (!finished) {
                process.destroy()
                runCatching { process.waitFor(250, java.util.concurrent.TimeUnit.MILLISECONDS) }
                if (process.isAlive) process.destroyForcibly()
                return false
            }
            val code = runCatching { process.exitValue() }
                .getOrDefault(LocalAdbClient.UNKNOWN_SHELL_EXIT_CODE)
            code == 0 && output.toString().contains("uid=0")
        } finally {
            if (process.isAlive) runCatching { process.destroyForcibly() }
        }
    }

    private const val DIRECT_SU_PROBE_TIMEOUT_MILLIS = 2_000L
}
