package dev.busung.s25uroot

object NativeProbe {
    @Volatile private var loaded = false
    @Volatile private var loadError: Throwable? = null

    init {
        loadError = runCatching { System.loadLibrary("s25u_native") }
            .onSuccess { loaded = true }
            .exceptionOrNull()
    }

    /** True when the native library loaded (arm64-v8a devices). */
    fun isAvailable(): Boolean = loaded

    /** Safe wrapper: never throws, falls back when the .so is missing (emulators). */
    fun runSafe(): String {
        if (!loaded) return "native_unavailable reason=${loadError?.javaClass?.simpleName ?: "missing-abi"}"
        return try {
            run()
        } catch (t: Throwable) {
            "native_run_failed ${t.javaClass.simpleName}: ${t.message}"
        }
    }

    /** Safe wrapper with /proc fallback when the .so is missing. */
    fun isKernelSuActiveSafe(): Boolean {
        if (!loaded) {
            return try {
                java.io.File("/sys/module/kernelsu").exists() ||
                    java.io.File("/proc/modules").takeIf { it.canRead() }?.useLines { lines ->
                        lines.any { it.startsWith("kernelsu ") || it.startsWith("kernelsu\t") }
                    } == true
            } catch (_: Throwable) {
                false
            }
        }
        return try {
            isKernelSuActive()
        } catch (_: Throwable) {
            false
        }
    }

    external fun run(): String

    external fun isKernelSuActive(): Boolean
}
