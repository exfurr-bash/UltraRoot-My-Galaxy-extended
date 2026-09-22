package dev.busung.s25uroot

import android.os.Build
import android.system.Os
import android.system.OsConstants

data class DeviceSnapshot(
    val manufacturer: String,
    val model: String,
    val device: String,
    val kernelRelease: String,
    val kernelVersionInfo: String,
    val machine: String,
    val buildId: String,
    val fingerprint: String,
    val androidRelease: String,
    val sdk: Int,
    val abi: String,
    val pageSize: Long,
) {
    val kernelBuildVersion: String
        get() = kernelVersionInfo

    val kernelVersion: String
        get() {
            // "6.6.98-android15-8-..." -> "6.6.98". Tolerate leading 'v',
            // trailing dots and empty releases instead of returning "" or "6.6.".
            val cleaned = kernelRelease.trim().removePrefix("v").removePrefix("V")
            val dotted = cleaned.takeWhile { it.isDigit() || it == '.' }.trimEnd('.')
            if (dotted.isBlank()) return ""
            // Collapse accidental ".." and drop empty segments.
            return dotted.split('.').filter(String::isNotEmpty).joinToString(".")
        }

    val kernelVersionFull: String
        get() = listOf(kernelRelease, kernelVersionInfo, machine)
            .filter(String::isNotBlank)
            .joinToString(" ")

    companion object {
        fun current(): DeviceSnapshot {
            val uname = Os.uname()
            return DeviceSnapshot(
                manufacturer = Build.MANUFACTURER,
                model = Build.MODEL,
                device = Build.DEVICE,
                kernelRelease = uname.release,
                kernelVersionInfo = uname.version,
                machine = uname.machine,
                buildId = Build.DISPLAY,
                fingerprint = Build.FINGERPRINT,
                androidRelease = Build.VERSION.RELEASE,
                sdk = Build.VERSION.SDK_INT,
                abi = Build.SUPPORTED_ABIS.firstOrNull().orEmpty(),
                pageSize = Os.sysconf(OsConstants._SC_PAGESIZE),
            )
        }
    }
}
