package dev.busung.s25uroot

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.busung.s25uroot.ui.hud.HudColors
import dev.busung.s25uroot.ui.hud.HudHeader
import dev.busung.s25uroot.ui.hud.hudCutShape
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Settings surface for RMG's independent, optional pre-root Shizuku bootstrap. */
@Composable
internal fun ShizukuBootSettingsCard() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val forgetDoneText = stringResource(R.string.wireless_adb_forget_done)
    val forgetFailedText = stringResource(R.string.wireless_adb_forget_failed)
    val pairingSearchingText = stringResource(R.string.adb_pair_searching)
    var enabled by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(AppPreferences.startShizukuOnBoot(context)) }
    var authToken by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(AppPreferences.shizukuAutomationToken(context)) }
    var diagnostic by remember { mutableStateOf(WirelessAdbDiagnostics.passiveSnapshot(context)) }
    var diagnosticsExpanded by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var testing by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    var showForgetDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    // Debounce token persistence: writing prefs on every keystroke is main-thread I/O.
    androidx.compose.runtime.LaunchedEffect(authToken) {
        kotlinx.coroutines.delay(500)
        AppPreferences.setShizukuAutomationToken(context, authToken)
    }

    if (showForgetDialog) {
        AlertDialog(
            onDismissRequest = { showForgetDialog = false },
            title = { Text(stringResource(R.string.wireless_adb_forget_title)) },
            text = { Text(stringResource(R.string.wireless_adb_forget_body)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showForgetDialog = false
                        ShizukuBootService.stop(context)
                        TemporaryWirelessAdb.forceDisable(context)
                        val forgotten = AdbCredentialStore.forgetLocalCredential(context)
                        diagnostic = WirelessAdbDiagnostics.passiveSnapshot(context).copy(
                            detail = if (forgotten) forgetDoneText else forgetFailedText,
                        )
                    },
                ) {
                    Text(stringResource(R.string.wireless_adb_forget_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgetDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = hudCutShape(10.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HudHeader(left = "SHIZUKU", right = "BOOT")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    Icons.Rounded.PowerSettingsNew,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = HudColors.Blood,
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.shizuku_boot_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = HudColors.Bone,
                    )
                    Text(
                        text = stringResource(R.string.shizuku_boot_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HudColors.BoneDim,
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = { checked ->
                        enabled = checked
                        AppPreferences.setStartShizukuOnBoot(context, checked)
                        if (!checked) {
                            ShizukuBootService.stop(context)
                            TemporaryWirelessAdb.forceDisable(context)
                        } else {
                            ShizukuBootService.startIfConfigured(context)
                        }
                    },
                )
            }

            AnimatedVisibility(visible = enabled) {
                OutlinedTextField(
                    value = authToken,
                    onValueChange = { value ->
                        authToken = value
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text(stringResource(R.string.shizuku_boot_auth_token))
                    },
                    supportingText = {
                        Text(stringResource(R.string.shizuku_boot_auth_token_description))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                )
            }

            HorizontalDivider()

            TextButton(
                onClick = { diagnosticsExpanded = !diagnosticsExpanded },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.wireless_adb_diagnostics_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = if (diagnosticsExpanded) {
                        Icons.Rounded.ExpandLess
                    } else {
                        Icons.Rounded.ExpandMore
                    },
                    contentDescription = stringResource(
                        if (diagnosticsExpanded) R.string.action_collapse else R.string.action_expand
                    ),
                )
            }

            AnimatedVisibility(visible = diagnosticsExpanded) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.wireless_adb_diagnostics_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    DiagnosticLine(
                        label = stringResource(R.string.wireless_adb_pairing_state),
                        value = wirelessAdbStatusLabel(diagnostic.authState),
                    )
                    DiagnosticLine(
                        label = stringResource(R.string.wireless_adb_key_state),
                        value = stringResource(
                            if (diagnostic.keyPresent) {
                                R.string.wireless_adb_key_present
                            } else {
                                R.string.wireless_adb_key_missing
                            },
                        ),
                    )
                    DiagnosticLine(
                        label = stringResource(R.string.wireless_adb_key_fingerprint),
                        value = diagnostic.fingerprint ?: "—",
                    )
                    DiagnosticLine(
                        label = stringResource(R.string.wireless_adb_debugging_state),
                        value = stringResource(
                            if (diagnostic.wirelessDebuggingEnabled) {
                                R.string.wireless_adb_enabled
                            } else {
                                R.string.wireless_adb_disabled
                            },
                        ),
                    )
                    DiagnosticLine(
                        label = stringResource(R.string.wireless_adb_connect_port),
                        value = diagnostic.connectPort?.toString() ?: "—",
                    )

                    Text(
                        text = diagnostic.detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    FilledTonalButton(
                        onClick = {
                            if (testing) return@FilledTonalButton
                            testing = true
                            scope.launch {
                                try {
                                    diagnostic = withContext(kotlinx.coroutines.Dispatchers.IO) {
                                        WirelessAdbDiagnostics.testConnection(context)
                                    }
                                } finally {
                                    testing = false
                                }
                            }
                        },
                        enabled = diagnostic.keyPresent && !testing,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (testing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                            )
                        }
                        Text(
                            text = stringResource(
                                if (testing) R.string.wireless_adb_testing else R.string.wireless_adb_test,
                            ),
                            modifier = if (testing) Modifier.padding(start = 8.dp) else Modifier,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = {
                                diagnostic = WirelessAdbDiagnostics.passiveSnapshot(context)
                            },
                            enabled = !testing,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(stringResource(R.string.wireless_adb_refresh))
                        }
                        OutlinedButton(
                            onClick = {
                                context.startActivity(
                                    AdbPairingSetupActivity.pairingIntent(
                                        context = context,
                                        forceRepair = diagnostic.keyPresent || diagnostic.credentialFlag,
                                    ),
                                )
                                diagnostic = diagnostic.copy(
                                    credentialFlag = false,
                                    authState = if (diagnostic.keyPresent) {
                                        WirelessAdbAuthState.SavedUnverified
                                    } else {
                                        WirelessAdbAuthState.NoCredential
                                    },
                                    detail = pairingSearchingText,
                                )
                            },
                            enabled = !testing,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(stringResource(R.string.wireless_adb_pair_repair))
                        }
                    }

                    OutlinedButton(
                        onClick = { openWirelessDebuggingSettings(context) },
                        enabled = !testing,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.wireless_adb_open_settings))
                    }

                    TextButton(
                        onClick = { showForgetDialog = true },
                        enabled = (diagnostic.keyPresent || diagnostic.credentialFlag) && !testing,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.wireless_adb_forget))
                    }

                    Text(
                        text = stringResource(R.string.wireless_adb_test_note),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(R.string.wireless_adb_repair_note),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun DiagnosticLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun wirelessAdbStatusLabel(state: WirelessAdbAuthState): String = stringResource(
    when (state) {
        WirelessAdbAuthState.NoCredential -> R.string.wireless_adb_status_no_credential
        WirelessAdbAuthState.SavedUnverified -> R.string.wireless_adb_status_saved_unverified
        WirelessAdbAuthState.Valid -> R.string.wireless_adb_status_valid
        WirelessAdbAuthState.PairingRejected -> R.string.wireless_adb_status_pairing_rejected
        WirelessAdbAuthState.MdnsUnavailable -> R.string.wireless_adb_status_mdns_unavailable
        WirelessAdbAuthState.PermissionRequired -> R.string.wireless_adb_status_permission_required
        WirelessAdbAuthState.ConnectionFailed -> R.string.wireless_adb_status_connection_failed
    },
)

private fun openWirelessDebuggingSettings(context: android.content.Context) {
    val wireless = Intent("android.settings.WIRELESS_DEBUGGING_SETTINGS")
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val opened = runCatching {
        context.startActivity(wireless)
        true
    }.getOrDefault(false)
    if (opened) return

    runCatching {
        context.startActivity(
            Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}
