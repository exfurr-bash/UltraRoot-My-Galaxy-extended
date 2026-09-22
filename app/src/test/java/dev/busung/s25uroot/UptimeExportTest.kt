package dev.busung.s25uroot

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UptimeExportTest {
    @Test
    fun normalizeSnapsToAllowedValues() {
        assertEquals(0, DiagnosticUptime.normalize(0))
        assertEquals(60, DiagnosticUptime.normalize(55))
        assertEquals(90, DiagnosticUptime.normalize(100))
        assertEquals(120, DiagnosticUptime.normalize(110))
        assertEquals(600, DiagnosticUptime.normalize(9999))
        assertEquals(0, DiagnosticUptime.normalize(-5))
    }

    @Test
    fun archiveFileNameCountsCompletedOnly() {
        val entries = listOf(
            historyEntry("a", InstallRunResult.Succeeded),
            historyEntry("b", InstallRunResult.Failed),
            historyEntry("c", InstallRunResult.Running),
        )
        val name = HistoryLogExporter.archiveFileName(entries)
        assertTrue(name.startsWith("RootMyGalaxy-logs-"))
        assertTrue(name.endsWith("-2.zip"))
    }

    private fun historyEntry(id: String, result: InstallRunResult) = InstallHistoryEntry(
        id = id,
        startedAtMillis = 1_700_000_000_000L,
        completedAtMillis = 1_700_000_006_000L,
        result = result,
        profileId = "a55x-A556EXXUEDZE4",
        usedShizuku = false,
        log = "log-$id",
    )
}
