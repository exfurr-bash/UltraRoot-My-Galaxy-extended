package dev.busung.s25uroot

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/**
 * Visible bridge for local Wireless ADB pairing.
 *
 * Normal callers keep the one-shot behavior, while diagnostics can explicitly
 * force re-pairing even when a historical pairing flag is still present. This
 * is required when adbd has discarded the device-side authorization but RMG's
 * local credential remains on disk.
 */
class AdbPairingSetupActivity : ComponentActivity() {
    private val requestNotifications = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            startPairingService()
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.adb_pair_notification_permission_required),
                Toast.LENGTH_LONG,
            ).show()
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val forceRepair = intent.getBooleanExtra(EXTRA_FORCE_REPAIR, false)
        if (forceRepair) {
            // A successful pairing service transaction sets this back to true.
            // Until then the saved boolean must not be treated as proof that
            // adbd still accepts the local TLS identity.
            AppPreferences.setAdbPaired(this, false)
        } else if (AppPreferences.adbPaired(this)) {
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        // POST_NOTIFICATIONS only exists on API 33+; older devices go straight
        // to the pairing service instead of requesting a phantom permission.
        if (android.os.Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            startPairingService()
            finish()
        } else {
            requestNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun startPairingService() {
        try {
            ContextCompat.startForegroundService(this, AdbPairingService.startIntent(this))
            setResult(RESULT_OK)
        } catch (e: Throwable) {
            // Android 12+ background FGS restrictions can reject the start from
            // a finishing bridge activity; surface instead of crashing.
            android.util.Log.w("AdbPairingSetup", "Unable to start pairing service", e)
            Toast.makeText(
                applicationContext,
                getString(R.string.adb_pair_notification_permission_required),
                Toast.LENGTH_LONG,
            ).show()
            setResult(RESULT_CANCELED)
        }
    }

    companion object {
        private const val EXTRA_FORCE_REPAIR = "force_repair"

        fun pairingIntent(context: Context, forceRepair: Boolean = false): Intent =
            Intent(context, AdbPairingSetupActivity::class.java)
                .putExtra(EXTRA_FORCE_REPAIR, forceRepair)
    }
}
