package dev.busung.s25uroot

import java.io.File
import java.security.MessageDigest
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BugFixRegressionTest {
    @get:Rule
    val tmp = TemporaryFolder()

    private fun snapshot(release: String) = DeviceSnapshot(
        manufacturer = "samsung",
        model = "SM-S938B",
        device = "pa3q",
        kernelRelease = release,
        kernelVersionInfo = "v",
        machine = "aarch64",
        buildId = "b",
        fingerprint = "f",
        androidRelease = "16",
        sdk = 36,
        abi = "arm64-v8a",
        pageSize = 4096,
    )

    @Test
    fun kernelVersionParsesStandardRelease() {
        assertEquals("6.6.98", snapshot("6.6.98-android15-8-abc").kernelVersion)
    }

    @Test
    fun kernelVersionToleratesLeadingVAndTrailingDot() {
        assertEquals("6.6.98", snapshot("v6.6.98-android15").kernelVersion)
        assertEquals("6.6", snapshot("6.6.-foo").kernelVersion)
        assertEquals("", snapshot("").kernelVersion)
        assertEquals("", snapshot("-generic").kernelVersion)
        assertEquals("6.6.98", snapshot("6.6.98.").kernelVersion)
    }

    @Test
    fun normalizeRoundHalfUpOnTies() {
        // Ties pick the larger entry so waits never round down silently.
        assertEquals(60, DiagnosticUptime.normalize(45))
        assertEquals(90, DiagnosticUptime.normalize(75))
        assertEquals(30, DiagnosticUptime.normalize(15))
        // 150 is equidistant 120/180 -> larger wins (180)
        assertEquals(180, DiagnosticUptime.normalize(150))
        // Existing behavior preserved
        assertEquals(60, DiagnosticUptime.normalize(55))
        assertEquals(90, DiagnosticUptime.normalize(100))
        assertEquals(120, DiagnosticUptime.normalize(110))
    }

    @Test
    fun fileMatchesArtifactIgnoresShaCase() {
        val file = tmp.newFile("a.bin").apply { writeBytes("hello".toByteArray()) }
        val lower = sha256Hex(file.readBytes())
        val upper = lower.uppercase()
        assertTrue(fileMatchesArtifact(file, RemoteArtifact("u", file.length(), lower)))
        assertTrue(fileMatchesArtifact(file, RemoteArtifact("u", file.length(), upper)))
    }

    @Test
    fun shouldRunForBootTrimsWhitespace() {
        assertFalse(shouldRunForBoot("  boot-a  ", "boot-a"))
        assertFalse(shouldRunForBoot("boot-a", "  boot-a  "))
        assertFalse(shouldRunForBoot("   ", "boot-a"))
    }

    @Test
    fun slideRouteParsesWithWhitespaceAndCase() {
        assertEquals(SlideRoute.Auto, SlideRoute.parse(" auto "))
        assertEquals(SlideRoute.Auto, SlideRoute.parse("AuTo"))
        assertEquals(SlideRoute.Tracefs, SlideRoute.parse(" tracefs "))
        assertEquals(SlideRoute.Default, SlideRoute.parse(" Default "))
        assertEquals(SlideRoute.Legacy, SlideRoute.parse("P0"))
    }

    @Test
    fun stagedFileSourceMissingFailsClosed() {
        val staged = tmp.newFile("staged.bin").apply { writeBytes(byteArrayOf(1, 2, 3)) }
        val missing = File(tmp.root, "absent.bin")
        assertFalse(missing.exists())
        assertFalse(stagedFileIsCurrent(staged, missing))
    }

    @Test
    fun stagedEmptyFilesAreCurrent() {
        val a = tmp.newFile("a.bin").apply { writeBytes(ByteArray(0)) }
        val b = tmp.newFile("b.bin").apply { writeBytes(ByteArray(0)) }
        assertTrue(stagedFileIsCurrent(a, b))
    }

    @Test
    fun supportManifestAcceptsUppercaseSha() {
        val sha = sha256Hex("x".toByteArray()).uppercase()
        val json = JSONObject()
            .put("schemaVersion", 3)
            .put("payloads", org.json.JSONArray().put(
                JSONObject()
                    .put("payloadId", "p1")
                    .put("displayName", "d")
                    .put("models", org.json.JSONArray().put("SM-S938B"))
                    .put("kernelVersions", org.json.JSONArray().put("6.6.98"))
                    .put("exploit", JSONObject().put("url", "https://raw.githubusercontent.com/exfurr-bash/UltraRoot-My-Galaxy-Payloads/main/a").put("size", 1).put("sha256", sha))
                    .put("kernelsu", JSONObject().put("url", "https://raw.githubusercontent.com/exfurr-bash/UltraRoot-My-Galaxy-Payloads/main/b").put("size", 1).put("sha256", sha)),
            ))
        val manifest = SupportManifest.parse(json.toString().toByteArray())
        assertEquals(sha.lowercase(), manifest.targets.single().exploit.sha256)
    }

    @Test
    fun shouldHealFreshInstallNeedsLiveRoot() {
        // Fresh install (no receipt) + live root => heal.
        assertTrue(shouldHealFreshInstall(false, null, "boot-a", liveRoot = true))
        // No live root => never heal, even without receipt.
        assertFalse(shouldHealFreshInstall(false, null, "boot-a", liveRoot = false))
        // Receipt already covers this boot => nothing to do.
        assertFalse(shouldHealFreshInstall(true, "boot-a", "boot-a", liveRoot = true))
        assertFalse(shouldHealFreshInstall(true, "  boot-a  ", "boot-a", liveRoot = true))
        // Stale receipt (other boot) + live root => heal to current boot.
        assertTrue(shouldHealFreshInstall(true, "boot-old", "boot-a", liveRoot = true))
        // Blank boot token => never heal.
        assertFalse(shouldHealFreshInstall(false, null, "   ", liveRoot = true))
    }

    @Test
    fun p0OffsetPatternAcceptsShortHex() {
        val pattern = Regex("slide-kaslr-ok[^\\n]*slide=([0-9a-fA-F]{1,16})")
        assertTrue(pattern.containsMatchIn("slide-kaslr-ok slide=170000"))
        assertTrue(pattern.containsMatchIn("slide-kaslr-ok slide=0000000000170000"))
        val short = pattern.find("slide-kaslr-ok slide=170000")!!.groupValues[1]
        assertEquals(0x170000L, short.toLong(16))
        // 4K page alignment mask
        assertEquals(0L, 0x170000L and 0xfffL)
    }

    private fun sha256Hex(bytes: ByteArray): String {
        val d = MessageDigest.getInstance("SHA-256").digest(bytes)
        return d.joinToString("") { "%02x".format(it) }
    }
}
