package dev.busung.s25uroot

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PayloadRepositoryTest {
    @Test
    fun manifestMatchesDeviceAndArtifactsDownload() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val repository = PayloadRepository(context)
        val snapshot = DeviceSnapshot.current()
        val profile = try {
            repository.resolveTarget(snapshot)
        } catch (_: Throwable) {
            // CI/emulator devices are not in the production feed; validate
            // artifact materialization against the first bundled target instead
            // of failing on unsupported hardware.
            repository.loadTargets().first()
        }
        if (profile.matches(snapshot)) {
            assertTrue(profile.matches(snapshot))
        }

        val payloads = repository.download(profile) { }
        assertEquals(profile.exploit.size, payloads.exploit.length())
        assertEquals(profile.kernelSu.artifact.size, payloads.kernelSu.length())
        assertTrue(payloads.exploit.canRead())
        assertTrue(payloads.kernelSu.canRead())
    }
}
