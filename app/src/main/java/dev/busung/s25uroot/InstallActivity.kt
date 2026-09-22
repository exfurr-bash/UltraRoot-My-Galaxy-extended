package dev.busung.s25uroot

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.busung.s25uroot.ui.hud.BloodBurstLottie
import dev.busung.s25uroot.ui.hud.BloodParticles
import dev.busung.s25uroot.ui.hud.BrutalHaptic
import dev.busung.s25uroot.ui.hud.HudColors
import dev.busung.s25uroot.ui.hud.HudCornerTicks
import dev.busung.s25uroot.ui.hud.HudDamageFlash
import dev.busung.s25uroot.ui.hud.HudHeader
import dev.busung.s25uroot.ui.hud.HudLed
import dev.busung.s25uroot.ui.hud.HudMotion
import dev.busung.s25uroot.ui.hud.HudSfx
import dev.busung.s25uroot.ui.hud.HudTypewriter
import dev.busung.s25uroot.ui.hud.PentagramSpinLottie
import dev.busung.s25uroot.ui.hud.StyleRankBadge
import dev.busung.s25uroot.ui.hud.UltrakillTitle
import dev.busung.s25uroot.ui.hud.brutalHaptic
import dev.busung.s25uroot.ui.hud.hudAnimatedScanlines
import dev.busung.s25uroot.ui.hud.hudBloodPulse
import dev.busung.s25uroot.ui.hud.hudCutShape
import dev.busung.s25uroot.ui.hud.hudScanlines
import dev.busung.s25uroot.ui.hud.hudShake
import dev.busung.s25uroot.ui.hud.rememberHudSound
import dev.busung.s25uroot.ui.hud.styleRankFor
import dev.busung.s25uroot.ui.theme.RootMyGalaxyTheme
import androidx.compose.foundation.BorderStroke
import kotlinx.coroutines.delay

class InstallActivity : ComponentActivity() {
    private val installViewModel by viewModels<InstallViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val profileId = intent.getStringExtra(EXTRA_PROFILE_ID)
        val rootMode = runCatching {
            RootMode.valueOf(intent.getStringExtra(EXTRA_ROOT_MODE) ?: RootMode.Online.name)
        }.getOrDefault(RootMode.Online)
        val startInstall = savedInstanceState == null && AppPreferences.consumeInstallRequest(
            this,
            intent.getStringExtra(EXTRA_INSTALL_REQUEST_ID),
        )
        intent.removeExtra(EXTRA_INSTALL_REQUEST_ID)
        setContent {
            RootMyGalaxyTheme(
                accentColor = AppPreferences.accentColor(this),
                themeMode = AppPreferences.themeMode(this),
            ) {
                val installState by installViewModel.state.collectAsStateWithLifecycle()
                val view = LocalView.current
                BackHandler(enabled = installState.busy) {
                    // Blocked back during install: give feedback instead of silence.
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    Toast.makeText(this, getString(R.string.install_busy_toast), Toast.LENGTH_SHORT).show()
                }
                LaunchedEffect(startInstall, profileId, rootMode) {
                    if (startInstall) installViewModel.install(profileId, rootMode)
                }
                InstallScreen(
                    installState = installState,
                    onRetry = { installViewModel.install(profileId, rootMode) },
                    onClose = ::finish,
                )
            }
        }
    }

    companion object {
        const val EXTRA_INSTALL_REQUEST_ID = "install_request_id"
        const val EXTRA_PROFILE_ID = "profile_id"
        const val EXTRA_ROOT_MODE = "root_mode"
    }
}

internal data class InstallerStep(
    @StringRes val title: Int,
    @StringRes val detail: Int,
    val icon: ImageVector,
)

internal val installerSteps = listOf(
    InstallerStep(R.string.step_support_title, R.string.step_support_detail, Icons.Rounded.Security),
    InstallerStep(R.string.step_download_title, R.string.step_download_detail, Icons.Rounded.CloudDownload),
    InstallerStep(R.string.step_exploit_title, R.string.step_exploit_detail, Icons.Rounded.Memory),
    InstallerStep(R.string.step_ksu_title, R.string.step_ksu_detail, Icons.Rounded.Check),
)

private fun clickHaptic(view: View) {
    view.performHapticFeedback(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            HapticFeedbackConstants.CONFIRM
        } else {
            HapticFeedbackConstants.LONG_PRESS
        },
    )
}

@Composable
private fun InstallScreen(
    installState: InstallUiState,
    onRetry: () -> Unit,
    onClose: () -> Unit,
) {
    val logScrollState = rememberScrollState()
    val view = LocalView.current
    val context = LocalContext.current
    val sound = rememberHudSound()
    val soundOn = remember(context) { AppPreferences.soundEnabled(context) }
    var flash by remember { mutableFloatStateOf(0f) }
    // Key on length, not the whole MB string; debounce and cancel stale scrolls.
    LaunchedEffect(installState.log.length) {
        delay(80)
        runCatching { logScrollState.scrollTo(logScrollState.maxValue) }
    }
    // Phase-change punch: flash + sound + haptics. Skip the initial composition
    // so entering the screen doesn't play a phase sound.
    var firstPhase by remember { mutableStateOf(true) }
    LaunchedEffect(installState.phase) {
        if (firstPhase) {
            firstPhase = false
            return@LaunchedEffect
        }
        flash = when (installState.phase) {
            InstallPhase.Failed -> 0.45f
            InstallPhase.Installed -> 0.35f
            else -> 0.18f
        }
        when (installState.phase) {
            InstallPhase.Installed -> {
                sound.play(HudSfx.RankUp, soundOn)
                brutalHaptic(view, BrutalHaptic.Success)
            }
            InstallPhase.Failed -> {
                sound.play(HudSfx.Error, soundOn)
                brutalHaptic(view, BrutalHaptic.Fail)
            }
            else -> {
                sound.play(HudSfx.Phase, soundOn)
                brutalHaptic(view, BrutalHaptic.Click)
            }
        }
        delay(220)
        flash = 0f
    }

    Scaffold(containerColor = HudColors.Void) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .hudAnimatedScanlines()
                .hudBloodPulse(installState.busy),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .hudShake(trigger = installState.phase, intensity = if (installState.phase == InstallPhase.Failed) 16f else 7f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(top = 28.dp, bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    HudHeader(left = "RITUAL", right = if (installState.busy) "ACTIVE" else "STANDBY")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        HudLed(
                            color = if (installState.phase == InstallPhase.Failed) HudColors.WarningAmber else HudColors.Blood,
                            blinking = installState.busy,
                        )
                        if (installState.busy) {
                            PentagramSpinLottie(spinning = true, size = 40.dp)
                        }
                        UltrakillTitle(
                            text = stringResource(R.string.install_title),
                            sub = when {
                                installState.mode == RootMode.Offline -> stringResource(R.string.install_subtitle_offline)
                                installState.busy -> stringResource(R.string.install_subtitle_online)
                                else -> null
                            },
                            modifier = Modifier.weight(1f),
                        )
                        StyleRankBadge(rank = styleRankFor(installState.phase))
                    }
                    HudTypewriter(
                        text = if (installState.busy) {
                            stringResource(R.string.install_keep_open)
                        } else {
                            installState.message
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = HudColors.BoneDim,
                        charsPerSecond = 80,
                    )
                }

                InstallerStatusCard(installState)
                InstallerSteps(installState.phase)
                InstallerLog(
                    output = installState.log,
                    modifier = Modifier.weight(1f),
                    scrollState = logScrollState,
                )

                if (!installState.busy) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (installState.phase == InstallPhase.Failed) {
                            FilledTonalButton(
                                onClick = {
                                    clickHaptic(view)
                                    onClose()
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(stringResource(R.string.action_close))
                            }
                            Button(
                                onClick = {
                                    brutalHaptic(view, BrutalHaptic.Heavy)
                                    onRetry()
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(stringResource(R.string.action_retry))
                            }
                        } else if (installState.phase == InstallPhase.Installed) {
                            Button(
                                onClick = {
                                    clickHaptic(view)
                                    onClose()
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.action_done))
                            }
                        }
                    }
                }
            }
            BloodParticles(burstKey = installState.phase.takeIf { it == InstallPhase.Installed || it == InstallPhase.Failed })
            if (installState.phase == InstallPhase.Installed) {
                BloodBurstLottie(
                    burstKey = installState.phase,
                    size = 260.dp,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            HudDamageFlash(alpha = flash)
        }
    }
}

@Composable
private fun InstallerStatusCard(installState: InstallUiState) {
    val failed = installState.phase == InstallPhase.Failed
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(
            1.dp,
            if (failed) HudColors.WarningAmber else HudColors.Blood.copy(alpha = 0.65f),
        ),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.PlateHigh,
            contentColor = HudColors.Bone,
        ),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HudCornerTicks(modifier = Modifier.matchParentSize())
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                HudHeader(
                    left = "STATUS",
                    right = if (installState.busy) "WORKING" else installState.phase.name.uppercase(),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AnimatedContent(targetState = installState.phase, label = "install-status-icon") { phase ->
                        when {
                            installState.busy -> LoadingIndicator(
                                modifier = Modifier.size(44.dp),
                                color = HudColors.Blood,
                            )
                            phase == InstallPhase.Installed -> Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = HudColors.Blood,
                            )
                            else -> Icon(
                                Icons.Rounded.Error,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = if (failed) HudColors.WarningAmber else HudColors.Blood,
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = installState.message,
                            style = MaterialTheme.typography.titleLarge,
                            color = HudColors.Bone,
                        )
                        Text(
                            text = installPhaseDetail(installState.phase),
                            color = HudColors.BoneDim,
                        )
                    }
                }
                BrutalProgressBar(
                    progress = installProgress(installState.phase),
                    failed = failed,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun BrutalProgressBar(
    progress: Float,
    failed: Boolean,
    modifier: Modifier = Modifier,
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(HudMotion.MEDIUM),
        label = "brutal-progress",
    )
    val barColor = if (failed) HudColors.WarningAmber else HudColors.Blood
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "// ${(animated * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = barColor,
                ),
            )
            Text(
                text = when {
                    animated >= 1f -> "[ COMPLETE ]"
                    animated <= 0f -> "[ STANDBY ]"
                    else -> "[ FEEDING ]"
                },
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = HudColors.Steel,
                ),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(hudCutShape(5.dp))
                .background(HudColors.Terminal)
                .border(
                    BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.5f)),
                    hudCutShape(5.dp),
                )
                .drawBehind {
                    val segments = 24
                    repeat(segments + 1) { i ->
                        val x = size.width * i / segments
                        drawLine(
                            color = HudColors.SteelDim.copy(alpha = 0.55f),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f,
                        )
                    }
                },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .clip(hudCutShape(5.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(HudColors.DarkRed, barColor, HudColors.BloodHot),
                        ),
                    )
                    .graphicsLayer {
                        shadowElevation = 14f
                        ambientShadowColor = barColor
                        spotShadowColor = barColor
                    },
            )
        }
    }
}

@Composable
private fun InstallerSteps(phase: InstallPhase) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HudHeader(left = "SEQUENCE", right = "04 PHASES")
            installerSteps.forEachIndexed { index, step ->
                val stepState = stepState(phase, index)
                val iconScale by animateFloatAsState(
                    targetValue = when (stepState) {
                        2 -> 1.1f
                        1 -> 1f
                        else -> 0.92f
                    },
                    animationSpec = androidx.compose.animation.core.spring(
                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                        stiffness = androidx.compose.animation.core.Spring.StiffnessMedium,
                    ),
                    label = "step-pop-$index",
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .graphicsLayer {
                                scaleX = iconScale
                                scaleY = iconScale
                                shadowElevation = if (stepState >= 1) 10f else 0f
                                ambientShadowColor = HudColors.Blood
                                spotShadowColor = HudColors.Blood
                            },
                        shape = hudCutShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (stepState >= 1) HudColors.Blood.copy(alpha = 0.6f)
                            else HudColors.SteelDim.copy(alpha = 0.4f),
                        ),
                        color = if (stepState >= 1) {
                            HudColors.DarkRed
                        } else {
                            HudColors.Gunmetal
                        },
                        contentColor = HudColors.Bone,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (stepState == 2) Icons.Rounded.Check else step.icon,
                                contentDescription = null,
                                modifier = Modifier.size(21.dp),
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(step.title),
                            style = MaterialTheme.typography.titleSmall,
                            color = HudColors.Bone,
                        )
                        Text(
                            text = stringResource(step.detail),
                            style = MaterialTheme.typography.bodySmall,
                            color = HudColors.BoneDim,
                        )
                    }
                    if (stepState == 1 && phase !in setOf(InstallPhase.Failed, InstallPhase.Ready)) {
                        LoadingIndicator(
                            modifier = Modifier.size(24.dp),
                            color = HudColors.Blood,
                        )
                    } else if (stepState == 0) {
                        HudLed(color = HudColors.SteelDim)
                    } else {
                        HudLed(color = HudColors.Blood)
                    }
                }
            }
        }
    }
}

@Composable
private fun InstallerLog(
    output: String,
    modifier: Modifier,
    scrollState: androidx.compose.foundation.ScrollState,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Terminal,
            contentColor = HudColors.Bone,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hudScanlines(alpha = 0.08f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HudHeader(left = "FEED", right = "LIVE")
            Text(
                stringResource(R.string.install_live_progress),
                style = MaterialTheme.typography.titleMedium,
                color = HudColors.Blood,
            )
            Text(
                text = output.ifBlank { stringResource(R.string.install_preparing) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = HudColors.Bone,
            )
        }
    }
}

@Composable
private fun installPhaseDetail(phase: InstallPhase): String = stringResource(
    when (phase) {
        InstallPhase.Checking -> R.string.phase_checking
        InstallPhase.Ready -> R.string.phase_ready
        InstallPhase.Downloading -> R.string.phase_downloading
        InstallPhase.Exploiting -> R.string.phase_exploiting
        InstallPhase.LoadingKernelSu -> R.string.phase_loading_ksu
        InstallPhase.Installed -> R.string.phase_installed
        InstallPhase.Failed -> R.string.phase_failed
    },
)

private fun installProgress(phase: InstallPhase): Float = when (phase) {
    InstallPhase.Checking -> 0.1f
    InstallPhase.Ready -> 0f
    InstallPhase.Downloading -> 0.3f
    InstallPhase.Exploiting -> 0.6f
    InstallPhase.LoadingKernelSu -> 0.85f
    InstallPhase.Installed -> 1f
    InstallPhase.Failed -> 0f
}

private fun stepState(phase: InstallPhase, stepIndex: Int): Int {
    if (phase == InstallPhase.Installed) return 2
    val activeIndex = when (phase) {
        InstallPhase.Checking, InstallPhase.Ready, InstallPhase.Failed -> 0
        InstallPhase.Downloading -> 1
        InstallPhase.Exploiting -> 2
        InstallPhase.LoadingKernelSu -> 3
        InstallPhase.Installed -> 4
    }
    return when {
        stepIndex < activeIndex -> 2
        stepIndex == activeIndex -> 1
        else -> 0
    }
}
