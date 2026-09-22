package dev.busung.s25uroot

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.busung.s25uroot.ui.hud.BloodBurstLottie
import dev.busung.s25uroot.ui.hud.BloodParticles
import dev.busung.s25uroot.ui.hud.BrutalHaptic
import dev.busung.s25uroot.ui.hud.HudBootIntro
import dev.busung.s25uroot.ui.hud.HudColors
import dev.busung.s25uroot.ui.hud.HudDamageFlash
import dev.busung.s25uroot.ui.hud.HudTypewriter
import dev.busung.s25uroot.ui.hud.HudMotion
import dev.busung.s25uroot.ui.hud.HudSfx
import dev.busung.s25uroot.ui.hud.StyleRankBadge
import dev.busung.s25uroot.ui.hud.UltrakillTitle
import dev.busung.s25uroot.ui.hud.brutalHaptic
import dev.busung.s25uroot.ui.hud.hudAnimatedScanlines
import dev.busung.s25uroot.ui.hud.hudBloodPulse
import dev.busung.s25uroot.ui.hud.hudGlitch
import dev.busung.s25uroot.ui.hud.hudShake
import dev.busung.s25uroot.ui.hud.rememberHudSound
import dev.busung.s25uroot.ui.hud.styleRankFor
import dev.busung.s25uroot.ui.hud.HudCornerTicks
import dev.busung.s25uroot.ui.hud.HudHeader
import dev.busung.s25uroot.ui.hud.HudLed
import dev.busung.s25uroot.ui.hud.HudSectionLabel
import dev.busung.s25uroot.ui.hud.hudCutShape
import dev.busung.s25uroot.ui.hud.hudPressScale
import dev.busung.s25uroot.ui.hud.hudScanlines
import dev.busung.s25uroot.ui.hud.hudVignette
import dev.busung.s25uroot.ui.theme.RootMyGalaxyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : ComponentActivity() {
    private val installViewModel by viewModels<InstallViewModel>()
    private var resumedOnce = false
    private var accentColor by mutableStateOf(AccentColor.Dynamic)
    private var themeMode by mutableStateOf(AppThemeMode.System)
    private var advancedMode by mutableStateOf(false)
    private var shizukuMode by mutableStateOf(false)
    private var restartZygoteAfterRoot by mutableStateOf(false)
    private var autoStartShizukuAfterRoot by mutableStateOf(true)
    private var soundEnabled by mutableStateOf(true)
    private var bootDone by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        bootDone = AppPreferences.bootSeen(this)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        accentColor = AppPreferences.accentColor(this)
        themeMode = AppPreferences.themeMode(this)
        advancedMode = AppPreferences.advancedMode(this)
        shizukuMode = AppPreferences.shizukuMode(this)
        restartZygoteAfterRoot = AppPreferences.restartZygoteAfterRoot(this)
        autoStartShizukuAfterRoot = AppPreferences.autoStartShizukuAfterRoot(this)
        soundEnabled = AppPreferences.soundEnabled(this)
        setContent {
            RootMyGalaxyTheme(accentColor = accentColor, themeMode = themeMode) {
                RootApp(
                    installViewModel = installViewModel,
                    accentColor = accentColor,
                    themeMode = themeMode,
                    advancedMode = advancedMode,
                    shizukuMode = shizukuMode,
                    restartZygoteAfterRoot = restartZygoteAfterRoot,
                    autoStartShizukuAfterRoot = autoStartShizukuAfterRoot,
                    soundEnabled = soundEnabled,
                    bootDone = bootDone,
                    onBootDone = {
                        AppPreferences.setBootSeen(this)
                        bootDone = true
                    },
                    onSoundChanged = { enabled ->
                        AppPreferences.setSoundEnabled(this, enabled)
                        soundEnabled = enabled
                    },
                    onAccentColorChanged = { color ->
                        AppPreferences.setAccentColor(this, color)
                        accentColor = color
                    },
                    onThemeModeChanged = { mode ->
                        AppPreferences.setThemeMode(this, mode)
                        themeMode = mode
                    },
                    onAdvancedModeChanged = { enabled ->
                        AppPreferences.setAdvancedMode(this, enabled)
                        advancedMode = enabled
                    },
                    onShizukuModeChanged = { enabled ->
                        AppPreferences.setShizukuMode(this, enabled)
                        shizukuMode = enabled
                    },
                    onRestartZygoteChanged = { enabled ->
                        AppPreferences.setRestartZygoteAfterRoot(this, enabled)
                        restartZygoteAfterRoot = enabled
                    },
                    onAutoStartShizukuChanged = { enabled ->
                        AppPreferences.setAutoStartShizukuAfterRoot(this, enabled)
                        autoStartShizukuAfterRoot = enabled
                    },
                    openInstaller = { profileId, mode ->
                        val installer = Intent(this, InstallActivity::class.java)
                            .putExtra(InstallActivity.EXTRA_INSTALL_REQUEST_ID, UUID.randomUUID().toString())
                        if (profileId != null) {
                            installer.putExtra(InstallActivity.EXTRA_PROFILE_ID, profileId)
                        }
                        installer.putExtra(InstallActivity.EXTRA_ROOT_MODE, mode.name)
                        startActivity(installer)
                    },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (resumedOnce) installViewModel.refresh() else resumedOnce = true
    }
}

private enum class AppPage(@StringRes val label: Int, val icon: ImageVector) {
    Overview(R.string.nav_overview, Icons.Rounded.Home),
    History(R.string.nav_history, Icons.Rounded.History),
    Settings(R.string.nav_settings, Icons.Rounded.Settings),
}

private data class LanguageOption(@StringRes val label: Int, val tag: String)

private enum class CompatibilityWarning {
    Device,
    KernelVersion,
}

private val languageOptions = listOf(
    LanguageOption(R.string.language_system, ""),
    LanguageOption(R.string.language_korean, "ko"),
    LanguageOption(R.string.language_english, "en"),
    LanguageOption(R.string.language_german, "de"),
    LanguageOption(R.string.language_japanese, "ja"),
    LanguageOption(R.string.language_chinese, "zh-CN"),
    LanguageOption(R.string.language_chinese_traditional, "zh-TW"),
    LanguageOption(R.string.language_turkish, "tr"),
    LanguageOption(R.string.language_brazillian_portuguese, "pt-BR"),
    LanguageOption(R.string.language_russian, "ru"),
    LanguageOption(R.string.language_vietnamese, "vi"),
    LanguageOption(R.string.language_uzbek, "uz"),
)

private const val KERNEL_SU_MANAGER_URL =
    "https://github.com/tiann/KernelSU/releases/download/v3.2.5/KernelSU_v3.2.5_32525-release.apk"
private const val KERNEL_SU_MANAGER_PACKAGE = "me.weishu.kernelsu"
private const val KERNEL_SU_HOME_URL = "https://kernelsu.org/"
private const val SHIZUKU_MANAGER_PACKAGE = "moe.shizuku.manager"
private const val SHIZUKU_MANAGER_URL = "https://github.com/thedjchi/Shizuku/releases/"

private fun isKernelSuManagerInstalled(context: Context): Boolean =
    context.packageManager.getLaunchIntentForPackage(KERNEL_SU_MANAGER_PACKAGE) != null

private fun openKernelSuManager(context: Context) {
    val launch = context.packageManager.getLaunchIntentForPackage(KERNEL_SU_MANAGER_PACKAGE)
    if (launch != null) {
        context.startActivity(launch)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(KERNEL_SU_MANAGER_URL)))
    }
}

private fun openShizukuManager(context: Context) {
    val launch = context.packageManager.getLaunchIntentForPackage(SHIZUKU_MANAGER_PACKAGE)
    if (launch != null) {
        context.startActivity(launch)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SHIZUKU_MANAGER_URL)))
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun RootApp(
    installViewModel: InstallViewModel,
    accentColor: AccentColor,
    themeMode: AppThemeMode,
    advancedMode: Boolean,
    shizukuMode: Boolean,
    restartZygoteAfterRoot: Boolean,
    autoStartShizukuAfterRoot: Boolean,
    soundEnabled: Boolean,
    bootDone: Boolean,
    onBootDone: () -> Unit,
    onSoundChanged: (Boolean) -> Unit,
    onAccentColorChanged: (AccentColor) -> Unit,
    onThemeModeChanged: (AppThemeMode) -> Unit,
    onAdvancedModeChanged: (Boolean) -> Unit,
    onShizukuModeChanged: (Boolean) -> Unit,
    onRestartZygoteChanged: (Boolean) -> Unit,
    onAutoStartShizukuChanged: (Boolean) -> Unit,
    openInstaller: (String?, RootMode) -> Unit,
) {
    val installState by installViewModel.state.collectAsStateWithLifecycle()
    val history by installViewModel.history.collectAsStateWithLifecycle()
    val targetCatalog by installViewModel.targetCatalog.collectAsStateWithLifecycle()
    var selectedPage by remember { mutableStateOf(AppPage.Overview) }
    var showInstallConfirmation by remember { mutableStateOf(false) }
    var showTargetPicker by remember { mutableStateOf(false) }
    var selectedProfile by remember { mutableStateOf<TargetProfile?>(null) }
    var compatibilityWarning by remember { mutableStateOf<CompatibilityWarning?>(null) }
    val device = remember { DeviceSnapshot.current() }
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val sound = rememberHudSound()
    val hazeState = remember { HazeState() }
    var updateStatus by remember { mutableStateOf<UpdateStatus>(UpdateStatus.Idle) }
    var updateCardDismissed by remember { mutableStateOf(false) }
    val checkForUpdate: () -> Unit = {
        if (!updateStatus.busy) {
            updateStatus = UpdateStatus.Checking
            scope.launch {
                val info = AppUpdater.fetchLatestRelease()
                updateStatus = when {
                    info == null -> UpdateStatus.Failed
                    AppUpdater.isUpdateAvailable(info.versionName, BuildConfig.VERSION_NAME) ->
                        UpdateStatus.Available(info)
                    else -> UpdateStatus.UpToDate
                }
            }
        }
    }
    val startDownload: (UpdateInfo) -> Unit = { info ->
        val apkUrl = info.apkUrl
        if (apkUrl == null) {
            AppUpdater.openReleasesPage(context)
        } else {
            updateStatus = UpdateStatus.Downloading(info, 0f)
            scope.launch {
                val apk = AppUpdater.downloadApk(context, apkUrl) { progress ->
                    updateStatus = UpdateStatus.Downloading(info, progress)
                }
                if (apk == null || !AppUpdater.installApk(context, apk)) {
                    Toast.makeText(context, context.getString(R.string.updater_download_failed), Toast.LENGTH_SHORT).show()
                    AppUpdater.openReleasesPage(context)
                }
                updateStatus = UpdateStatus.Available(info)
            }
        }
    }
    LaunchedEffect(Unit) { checkForUpdate() }

    if (showTargetPicker) {
        TargetSelectionSheet(
            device = device,
            catalog = targetCatalog,
            hazeState = hazeState,
            onDismiss = { showTargetPicker = false },
            onRetry = installViewModel::loadTargetCatalog,
            onNext = { profile ->
                selectedProfile = profile
                showTargetPicker = false
                compatibilityWarning = when {
                    !profile.matchesDevice(device) -> CompatibilityWarning.Device
                    !profile.matchesKernelVersion(device) -> CompatibilityWarning.KernelVersion
                    else -> null
                }
                if (compatibilityWarning == null) showInstallConfirmation = true
            },
        )
    }

    compatibilityWarning?.let { warning ->
        val profile = selectedProfile ?: return@let
        AlertDialog(
            onDismissRequest = {
                compatibilityWarning = null
                showTargetPicker = true
            },
            icon = { Icon(Icons.Rounded.Warning, contentDescription = null) },
            title = {
                DialogDimAmount(0.34f)
                Text(
                    stringResource(when (warning) {
                        CompatibilityWarning.Device -> R.string.device_mismatch_title
                        CompatibilityWarning.KernelVersion -> R.string.kernel_version_mismatch_title
                    }),
                )
            },
            text = {
                Text(
                    when (warning) {
                        CompatibilityWarning.Device -> stringResource(
                            R.string.device_mismatch_body,
                            device.model,
                            profile.supportedModels,
                        )
                        CompatibilityWarning.KernelVersion -> stringResource(
                            R.string.kernel_version_mismatch_body,
                            device.kernelVersion,
                            profile.supportedKernelVersions,
                        )
                    },
                )
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        clickHaptic(view)
                        compatibilityWarning = when (warning) {
                            CompatibilityWarning.Device -> if (!profile.matchesKernelVersion(device)) {
                                CompatibilityWarning.KernelVersion
                            } else {
                                null
                            }
                            CompatibilityWarning.KernelVersion -> null
                        }
                        if (compatibilityWarning == null) {
                            showInstallConfirmation = true
                        }
                    },
                ) {
                    Text(stringResource(R.string.action_continue))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        clickHaptic(view)
                        compatibilityWarning = null
                        showTargetPicker = true
                    },
                ) {
                    Text(stringResource(R.string.action_back))
                }
            },
        )
    }

    if (showInstallConfirmation) {
        var rootMode by remember { mutableStateOf(RootMode.Online) }
        val offlineProfile = remember(showInstallConfirmation) {
            runCatching { KnownGoodPayloadStore.load(context).profile }.getOrNull()
        }
        if (offlineProfile == null && rootMode == RootMode.Offline) {
            rootMode = RootMode.Online
        }
        AlertDialog(
            onDismissRequest = { showInstallConfirmation = false },
            icon = { Icon(Icons.Rounded.Security, contentDescription = null) },
            title = {
                DialogDimAmount(0.34f)
                Text(stringResource(R.string.install_confirm_title))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.install_confirm_body))
                    HudHeader(left = "MODE", right = if (rootMode == RootMode.Online) "NET" else "CACHE")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                    ) {
                        RootMode.entries.forEachIndexed { index, mode ->
                            val enabled = mode == RootMode.Online || offlineProfile != null
                            ToggleButton(
                                checked = rootMode == mode,
                                onCheckedChange = {
                                    clickHaptic(view)
                                    sound.play(HudSfx.Click, soundEnabled)
                                    rootMode = mode
                                },
                                enabled = enabled,
                                modifier = Modifier.weight(1f),
                                colors = ToggleButtonDefaults.toggleButtonColors(
                                    containerColor = HudColors.Plate,
                                    checkedContainerColor = HudColors.DarkRed,
                                    checkedContentColor = HudColors.Bone,
                                ),
                                shapes = when (index) {
                                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                    else -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                },
                            ) {
                                Text(
                                    stringResource(
                                        when (mode) {
                                            RootMode.Online -> R.string.root_mode_online
                                            RootMode.Offline -> R.string.root_mode_offline
                                        },
                                    ),
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                    Text(
                        text = when {
                            rootMode == RootMode.Offline && offlineProfile != null ->
                                stringResource(R.string.offline_cached_format, offlineProfile.displayName)
                            else -> stringResource(R.string.root_mode_offline_unavailable)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = HudColors.BoneDim,
                    )
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = {
                    clickHaptic(view)
                    showInstallConfirmation = false
                    openInstaller(selectedProfile?.profileId, rootMode)
                    selectedProfile = null
                }) {
                    Text(stringResource(R.string.action_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    clickHaptic(view)
                    showInstallConfirmation = false
                }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }

    var damageFlash by remember { mutableStateOf(0f) }
    LaunchedEffect(damageFlash) {
        if (damageFlash > 0f) {
            delay(180)
            damageFlash = 0f
        }
    }
    // Punch the screen when install fails or succeeds from background refresh.
    LaunchedEffect(installState.phase) {
        if (installState.phase == InstallPhase.Failed) damageFlash = 0.4f
    }

    SharedTransitionLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HudColors.Void)
                .hazeSource(hazeState)
                .hudBloodPulse(installState.busy),
        ) {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier.hazeEffect(
                            hazeState,
                            style = HazeStyle(
                                backgroundColor = HudColors.Gunmetal.copy(alpha = 0.72f),
                                blurRadius = 20.dp,
                                tints = emptyList(),
                            ),
                        ),
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                    ) {
                        AppPage.entries.forEach { page ->
                            val selected = selectedPage == page
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    clickHaptic(view)
                                    sound.play(HudSfx.Click, soundEnabled)
                                    if (selectedPage != page) {
                                        selectedPage = page
                                    }
                                },
                                modifier = Modifier.padding(top = 4.dp),
                                icon = {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (selected) {
                                            HudLed(
                                                color = HudColors.Blood,
                                                blinking = installState.busy,
                                                modifier = Modifier.align(Alignment.TopEnd),
                                            )
                                        }
                                        Icon(page.icon, contentDescription = null)
                                    }
                                },
                                label = { Text(stringResource(page.label)) },
                            )
                        }
                    }
                },
                containerColor = Color.Transparent,
            ) { padding ->
                AnimatedContent(
                    targetState = selectedPage,
                    label = "page",
                    transitionSpec = {
                        val forward = targetState.ordinal > initialState.ordinal
                        val slideDir = if (forward) 1 else -1
                        (slideInHorizontally(
                            animationSpec = tween(HudMotion.ULTRA_ENTER),
                            initialOffsetX = { it / 4 * slideDir },
                        ) + fadeIn(tween(HudMotion.MEDIUM)) + scaleIn(
                            initialScale = 0.96f,
                            animationSpec = tween(HudMotion.ULTRA_ENTER),
                        )) togetherWith
                            (slideOutHorizontally(
                                animationSpec = tween(HudMotion.MEDIUM),
                                targetOffsetX = { -it / 4 * slideDir },
                            ) + fadeOut(tween(HudMotion.MEDIUM)))
                    },
                ) { page ->
                    when (page) {
                        AppPage.Overview -> OverviewPage(
                            padding = padding,
                            device = device,
                            installState = installState,
                            updateStatus = updateStatus,
                            updateCardDismissed = updateCardDismissed,
                            onDismissUpdateCard = { updateCardDismissed = true },
                            onStartDownload = startDownload,
                            soundEnabled = soundEnabled,
                            onInstall = {
                                selectedProfile = null
                                // Fallback manual: se o auto-detect falhou (fase Failed),
                                // oferece o catálogo completo mesmo com o Advanced
                                // desligado, em vez de repetir o automático que
                                // acabou de falhar. O picker já mostra os avisos
                                // de compatibilidade por perfil.
                                if (advancedMode || installState.phase == InstallPhase.Failed) {
                                    showTargetPicker = true
                                    installViewModel.loadTargetCatalog()
                                } else {
                                    showInstallConfirmation = true
                                }
                            },
                        )
                        AppPage.History -> HistoryPage(
                            padding,
                            history,
                            sharedScope = this@SharedTransitionLayout,
                            onDeleteEntries = installViewModel::deleteHistoryEntries,
                        )
                        AppPage.Settings -> SettingsPage(
                            padding = padding,
                            accentColor = accentColor,
                            themeMode = themeMode,
                            advancedMode = advancedMode,
                            shizukuMode = shizukuMode,
                            rootActive = installState.phase == InstallPhase.Installed,
                            restartZygoteAfterRoot = restartZygoteAfterRoot,
                            autoStartShizukuAfterRoot = autoStartShizukuAfterRoot,
                            soundEnabled = soundEnabled,
                            onSoundChanged = onSoundChanged,
                            updateStatus = updateStatus,
                            onCheckForUpdate = checkForUpdate,
                            onStartDownload = startDownload,
                            onAccentColorChanged = onAccentColorChanged,
                            onThemeModeChanged = onThemeModeChanged,
                            onAdvancedModeChanged = onAdvancedModeChanged,
                            onShizukuModeChanged = onShizukuModeChanged,
                            onRestartZygoteChanged = onRestartZygoteChanged,
                            onAutoStartShizukuChanged = onAutoStartShizukuChanged,
                        )
                    }
                }
            }
            HudDamageFlash(alpha = damageFlash)
            if (!bootDone) {
                HudBootIntro(onDone = onBootDone)
            }
        }
    }
}

@Composable
private fun AppVersionText(
    style: TextStyle,
    color: Color,
) {
    Text(
        text = stringResource(
            R.string.version_format,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
        ),
        style = style,
        color = color,
    )
}

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
private fun DialogDimAmount(amount: Float) {
    val window = (LocalView.current.parent as DialogWindowProvider).window
    SideEffect { window.setDimAmount(amount) }
}

@Composable
private fun OverviewPage(
    padding: PaddingValues,
    device: DeviceSnapshot,
    installState: InstallUiState,
    updateStatus: UpdateStatus,
    updateCardDismissed: Boolean,
    onDismissUpdateCard: () -> Unit,
    onStartDownload: (UpdateInfo) -> Unit,
    soundEnabled: Boolean,
    onInstall: () -> Unit,
) {
    val sound = rememberHudSound()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).hudAnimatedScanlines().hudBloodPulse(installState.busy),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            AnimatedVisibility(
                visible = true,
                enter = HudMotion.brutalEnter(0),
                label = "hero-enter",
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 54.dp, bottom = 14.dp)
                        .hudGlitch(if (installState.phase == InstallPhase.Failed) 1f else 0f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    HudHeader(left = "SYS", right = if (installState.busy) "RITUAL ACTIVE" else "ONLINE")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        HudLed(
                            color = if (installState.busy) HudColors.WarningAmber else HudColors.Blood,
                            blinking = installState.busy,
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_app_logo),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = HudColors.Blood,
                        )
                        AppVersionText(
                            style = MaterialTheme.typography.bodyMedium,
                            color = HudColors.Steel.copy(alpha = 0.7f),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        StyleRankBadge(rank = styleRankFor(installState.phase))
                    }
                    UltrakillTitle(
                        text = stringResource(R.string.app_name),
                        sub = "blood is fuel // hell is full",
                    )
                    HudTypewriter(
                        text = if (installState.busy) "RITUAL IN PROGRESS — KEEP THIS VESSEL AWAKE" else installState.message.ifBlank { "AWAITING ORDERS, SINNER" },
                        style = MaterialTheme.typography.labelLarge,
                        color = HudColors.BoneDim,
                        charsPerSecond = 70,
                    )
                }
            }
        }
        if (
            !updateCardDismissed &&
            updateStatus.info != null
        ) {
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = HudMotion.brutalEnter(HudMotion.STAGGER),
                    label = "update-enter",
                ) {
                    UpdateCard(
                        status = updateStatus,
                        onDismiss = onDismissUpdateCard,
                        onStartDownload = onStartDownload,
                    )
                }
            }
        }
        item {
            AnimatedVisibility(
                visible = true,
                enter = HudMotion.brutalEnter(HudMotion.STAGGER * 2),
                label = "install-enter",
            ) {
                InstallStatusCard(installState, onInstall, soundEnabled)
            }
        }
        item {
            AnimatedVisibility(
                visible = true,
                enter = HudMotion.brutalEnter(HudMotion.STAGGER * 3),
                label = "device-enter",
            ) {
                DeviceCard(device)
            }
        }
        item {
            AnimatedVisibility(
                visible = true,
                enter = HudMotion.brutalEnter(HudMotion.STAGGER * 4),
                label = "how-enter",
            ) {
                HowItWorksCard()
            }
        }
    }
}

private sealed interface UpdateStatus {
    data object Idle : UpdateStatus
    data object Checking : UpdateStatus
    data class Available(val info: UpdateInfo) : UpdateStatus
    data class Downloading(val info: UpdateInfo, val progress: Float) : UpdateStatus
    data object UpToDate : UpdateStatus
    data object Failed : UpdateStatus
}

private val UpdateStatus.busy: Boolean
    get() = this is UpdateStatus.Checking || this is UpdateStatus.Downloading

private val UpdateStatus.info: UpdateInfo?
    get() = when (this) {
        is UpdateStatus.Available -> this.info
        is UpdateStatus.Downloading -> this.info
        else -> null
    }

@Composable
private fun UpdateCard(
    status: UpdateStatus,
    onDismiss: () -> Unit,
    onStartDownload: (UpdateInfo) -> Unit,
) {
    val view = LocalView.current
    val info = status.info
    if (info == null) return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(1.dp, HudColors.Blood.copy(alpha = 0.55f)),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.PlateHigh,
            contentColor = HudColors.Bone,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HudHeader(left = "UPDATE", right = "ALERT")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    Icons.Rounded.SystemUpdate,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = HudColors.Blood,
                )
                Text(
                    text = stringResource(R.string.updater_available_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = {
                        clickHaptic(view)
                        onDismiss()
                    },
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.action_close),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            Text(
                text = stringResource(R.string.updater_available_body, info.versionName),
                style = MaterialTheme.typography.bodyMedium,
            )
            when (status) {
                is UpdateStatus.Downloading -> {
                    LinearProgressIndicator(
                        progress = { status.progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = LocalContentColor.current,
                        trackColor = LocalContentColor.current.copy(alpha = 0.2f),
                        drawStopIndicator = {},
                    )
                    Text(
                        text = stringResource(R.string.updater_downloading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalContentColor.current.copy(alpha = 0.78f),
                    )
                }
                else -> {
                    FilledTonalButton(onClick = {
                        clickHaptic(view)
                        onStartDownload(info)
                    }) {
                        Text(stringResource(R.string.updater_button_download))
                    }
                }
            }
        }
    }
}

@Composable
private fun HowItWorksCard() {
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
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            HudHeader(left = "PROTOCOL", right = "04 STEPS")
            Text(
                stringResource(R.string.how_it_works),
                style = MaterialTheme.typography.titleMedium,
                color = HudColors.Bone,
            )
            installerSteps.forEach { step ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = hudCutShape(8.dp),
                        color = HudColors.DarkRed,
                        contentColor = HudColors.Bone,
                        border = BorderStroke(1.dp, HudColors.Blood.copy(alpha = 0.4f)),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(step.icon, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(step.title),
                            style = MaterialTheme.typography.titleSmall,
                            color = HudColors.Bone,
                        )
                        Text(
                            stringResource(step.detail),
                            style = MaterialTheme.typography.bodySmall,
                            color = HudColors.BoneDim,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstallStatusCard(installState: InstallUiState, onInstall: () -> Unit, soundEnabled: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current
    val sound = rememberHudSound()
    val interactionSource = remember { MutableInteractionSource() }
    val uriHandler = LocalUriHandler.current
    val managerInstalled = remember(installState) { isKernelSuManagerInstalled(context) }
    val failed = installState.phase == InstallPhase.Failed
    val installed = installState.phase == InstallPhase.Installed
    val borderColor by animateColorAsState(
        targetValue = when {
            failed -> HudColors.WarningAmber
            installed -> HudColors.BloodHot
            installState.busy -> HudColors.Blood
            else -> HudColors.Blood.copy(alpha = 0.65f)
        },
        animationSpec = tween<Color>(HudMotion.MEDIUM),
        label = "install-border",
    )
    Card(
        onClick = {
            when {
                installState.busy -> brutalHaptic(view, BrutalHaptic.Click)
                installed -> {
                    brutalHaptic(view, BrutalHaptic.Success)
                    sound.play(HudSfx.Confirm, soundEnabled)
                    if (managerInstalled) {
                        openKernelSuManager(context)
                    } else {
                        uriHandler.openUri(KERNEL_SU_MANAGER_URL)
                    }
                }
                else -> {
                    brutalHaptic(view, BrutalHaptic.Heavy)
                    sound.play(HudSfx.Phase, soundEnabled)
                    onInstall()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .hudPressScale(interactionSource)
            .hudShake(trigger = installState.phase.takeIf { failed }, intensity = 16f),
        shape = hudCutShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        interactionSource = interactionSource,
        colors = CardDefaults.cardColors(
            containerColor = HudColors.PlateHigh,
            contentColor = HudColors.Bone,
        ),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HudCornerTicks(modifier = Modifier.matchParentSize())
            BloodParticles(burstKey = installState.phase.takeIf { installed })
            if (installed) {
                BloodBurstLottie(
                    burstKey = installState.phase,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    size = 140.dp,
                )
            }
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                when {
                    installState.busy -> LoadingIndicator(
                        modifier = Modifier.size(44.dp),
                        color = HudColors.Blood,
                    )
                    installed -> Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = HudColors.BloodHot,
                    )
                    failed -> Icon(
                        Icons.Rounded.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = HudColors.WarningAmber,
                    )
                    else -> Icon(
                        Icons.Rounded.Warning, contentDescription = null, modifier = Modifier.size(44.dp),
                        tint = HudColors.Blood,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    if (installed) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_kernelsu),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = HudColors.BloodHot,
                            )
                            Text(
                                text = stringResource(R.string.status_ksu_active),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    } else {
                        Text(
                            text = when (installState.phase) {
                                InstallPhase.Ready -> stringResource(R.string.status_not_installed)
                                else -> installState.message
                            },
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Text(
                        text = when (installState.phase) {
                            InstallPhase.Installed -> stringResource(
                                if (managerInstalled) {
                                    R.string.install_tap_open_manager
                                } else {
                                    R.string.install_tap_manager
                                },
                            )
                            InstallPhase.Failed -> stringResource(R.string.install_tap_retry)
                            else -> stringResource(R.string.install_tap_start)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = HudColors.BoneDim,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(device: DeviceSnapshot) {
    val view = LocalView.current
    var kernelExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            HudHeader(left = "DEVICE", right = "SCAN")
            InfoRow(Icons.Rounded.Memory, stringResource(R.string.device), "${device.manufacturer} ${device.model} (${device.device})")
            InfoRow(Icons.Rounded.Code, stringResource(R.string.firmware), device.buildId)
            InfoRow(Icons.Rounded.Info, stringResource(R.string.system), "Android ${device.androidRelease} (API ${device.sdk})")
            InfoRow(
                icon = Icons.Rounded.Info,
                label = stringResource(R.string.kernel),
                value = if (kernelExpanded) device.kernelVersionFull else device.kernelRelease,
                onClick = {
                    clickHaptic(view)
                    kernelExpanded = !kernelExpanded
                },
            )
            InfoRow(Icons.Rounded.Security, stringResource(R.string.system_abi), "${device.abi} (${device.pageSize / 1024}K)")
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = if (onClick != null) {
            Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick = onClick)
        } else {
            Modifier
        },
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Icon(icon, contentDescription = null, tint = HudColors.Blood)
        Column {
            Text(label, style = MaterialTheme.typography.titleSmall, color = HudColors.Bone)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = HudColors.BoneDim)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun HistoryPage(
    padding: PaddingValues,
    history: List<InstallHistoryEntry>,
    sharedScope: SharedTransitionScope,
    onDeleteEntries: (Set<String>) -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current
    var selectedHistoryId by remember { mutableStateOf<String?>(null) }
    var selectionIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var pendingDeleteIds by remember { mutableStateOf<Set<String>?>(null) }
    val selectedEntry = history.firstOrNull { it.id == selectedHistoryId }
    val selectableIds = history
        .filter { it.result != InstallRunResult.Running }
        .map { it.id }
        .toSet()
    val selecting = selectionIds.isNotEmpty()
    val exportZipLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        result.data?.data?.let { uri -> HistoryLogExporter.save(context, uri, history) }
    }
    BackHandler(enabled = selectedEntry != null || selecting) {
        if (selecting) {
            selectionIds = emptySet()
        } else {
            selectedHistoryId = null
        }
    }

    pendingDeleteIds?.let { ids ->
        AlertDialog(
            onDismissRequest = { pendingDeleteIds = null },
            icon = { Icon(Icons.Rounded.Delete, contentDescription = null) },
            title = {
                DialogDimAmount(0.34f)
                Text(pluralStringResource(R.plurals.history_delete_selected_title, ids.size, ids.size))
            },
            text = { Text(pluralStringResource(R.plurals.history_delete_selected_body, ids.size, ids.size)) },
            confirmButton = {
                FilledTonalButton(onClick = {
                    clickHaptic(view)
                    onDeleteEntries(ids)
                    selectionIds = emptySet()
                    pendingDeleteIds = null
                }) {
                    Text(stringResource(R.string.history_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    clickHaptic(view)
                    pendingDeleteIds = null
                }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }

    AnimatedContent(
        targetState = selectedEntry,
        contentKey = { it?.id ?: "history-list" },
        label = "history-detail",
        transitionSpec = {
            (fadeIn(tween(HudMotion.MEDIUM)) + scaleIn(
                initialScale = 0.96f,
                animationSpec = tween(HudMotion.ULTRA_ENTER),
            )) togetherWith fadeOut(tween(HudMotion.FAST))
        },
    ) { entry ->
        if (entry == null) {
            HistoryList(
                padding = padding,
                history = history,
                sharedScope = sharedScope,
                animatedScope = this,
                selectionIds = selectionIds,
                selectableIds = selectableIds,
                onToggleSelection = { id ->
                    selectionIds = if (id in selectionIds) {
                        selectionIds - id
                    } else {
                        selectionIds + id
                    }
                },
                onSelectAll = {
                    selectionIds = if (selectionIds.size == selectableIds.size) {
                        emptySet()
                    } else {
                        selectableIds
                    }
                },
                onClearSelection = { selectionIds = emptySet() },
                onEntryClick = { selectedHistoryId = it.id },
                onDeleteSelected = { pendingDeleteIds = selectionIds },
                onExportZip = {
                    exportZipLauncher.launch(
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/zip"
                            putExtra(
                                Intent.EXTRA_TITLE,
                                HistoryLogExporter.archiveFileName(history),
                            )
                        },
                    )
                },
            )
        } else {
            HistoryDetail(
                padding = padding,
                entry = entry,
                sharedScope = sharedScope,
                animatedScope = this,
                onBack = { selectedHistoryId = null },
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun HistoryList(
    padding: PaddingValues,
    history: List<InstallHistoryEntry>,
    sharedScope: SharedTransitionScope,
    animatedScope: androidx.compose.animation.AnimatedVisibilityScope,
    selectionIds: Set<String>,
    selectableIds: Set<String>,
    onToggleSelection: (String) -> Unit,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onEntryClick: (InstallHistoryEntry) -> Unit,
    onDeleteSelected: () -> Unit,
    onExportZip: () -> Unit,
) {
    val view = LocalView.current
    val selecting = selectionIds.isNotEmpty()
    val hasCompleted = remember(history) {
        history.any { it.result != InstallRunResult.Running }
    }
    Box(modifier = Modifier.fillMaxSize().padding(padding).hudScanlines().hudVignette()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 96.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HudHeader(left = "ARCHIVE", right = "RUNS")
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f).height(48.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = stringResource(R.string.history_title),
                            style = MaterialTheme.typography.headlineLarge,
                            color = HudColors.Bone,
                        )
                    }
                    if (hasCompleted && !selecting) {
                        IconButton(onClick = {
                            clickHaptic(view)
                            onExportZip()
                        }) {
                            Icon(
                                Icons.Rounded.CloudDownload,
                                contentDescription = stringResource(R.string.export_logs_zip),
                                tint = HudColors.Blood,
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = selecting,
                        enter = fadeIn() + scaleIn(initialScale = 0.9f),
                        exit = fadeOut() + scaleOut(targetScale = 0.9f),
                    ) {
                        Row {
                            IconButton(onClick = {
                                clickHaptic(view)
                                onSelectAll()
                            }) {
                                Icon(
                                    Icons.Rounded.SelectAll,
                                    contentDescription = stringResource(R.string.history_select_all),
                                )
                            }
                            IconButton(onClick = {
                                clickHaptic(view)
                                onClearSelection()
                            }) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.history_clear_selection),
                                )
                            }
                        }
                    }
                }
                }
            }
            if (history.isEmpty()) {
                item { EmptyHistoryCard() }
            } else {
                itemsIndexed(history, key = { _, entry -> entry.id }) { _, entry ->
                    with(sharedScope) {
                        HistoryEntryCard(
                            entry = entry,
                            heroModifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(key = "history-${entry.id}"),
                                    animatedVisibilityScope = animatedScope,
                                ),
                            selectionMode = selecting,
                        isSelected = entry.id in selectionIds,
                        selectable = entry.id in selectableIds,
                        onClick = {
                            if (selecting) {
                                onToggleSelection(entry.id)
                            } else {
                                onEntryClick(entry)
                            }
                        },
                        onLongClick = {
                            if (entry.id in selectableIds) onToggleSelection(entry.id)
                        },
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = selecting,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            enter = fadeIn() + scaleIn(initialScale = 0.85f),
            exit = fadeOut() + scaleOut(targetScale = 0.85f),
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    clickHaptic(view)
                    onDeleteSelected()
                },
                icon = { Icon(Icons.Rounded.Delete, contentDescription = null) },
                text = { Text(stringResource(R.string.history_delete_selected, selectionIds.size)) },
            )
        }
    }
}

@Composable
private fun EmptyHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(
                Icons.Rounded.History,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = HudColors.Blood,
            )
            Column {
                Text(stringResource(R.string.history_empty_title), style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(R.string.history_empty_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HudColors.BoneDim,
                )
            }
        }
    }
}

@Composable
private fun HistoryEntryCard(
    entry: InstallHistoryEntry,
    selectionMode: Boolean,
    isSelected: Boolean,
    selectable: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    heroModifier: Modifier = Modifier,
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val shape = expressiveClickableCardShape(interactionSource)
    val containerColor = historyResultContainerColor(entry.result)
    val contentColor = historyResultContentColor(entry.result)
    val borderWidth by animateDpAsState(
        targetValue = if (selectionMode && isSelected) 2.dp else 0.dp,
        label = "history-card-border",
    )
    Card(
        modifier = heroModifier
            .fillMaxWidth()
            .clip(shape)
            .combinedClickable(
                interactionSource = interactionSource,
                onClick = {
                    clickHaptic(view)
                    onClick()
                },
                onLongClick = {
                    clickHaptic(view)
                    onLongClick()
                },
            ),
        shape = shape,
        border = if (borderWidth > 0.dp) {
            BorderStroke(borderWidth, HudColors.Blood)
        } else {
            BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.35f))
        },
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 15.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Crossfade(
                targetState = selectionMode,
                label = "history-leading",
                modifier = Modifier.size(48.dp),
            ) { selecting ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (selecting) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            enabled = selectable,
                        )
                    } else {
                        Icon(historyResultIcon(entry.result), contentDescription = null, modifier = Modifier.size(30.dp))
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(historyResultLabel(entry.result), style = MaterialTheme.typography.titleMedium)
                Text(
                    formatHistoryTime(entry.startedAtMillis),
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.78f),
                )
            }
            if (!selectionMode) {
                Icon(Icons.Rounded.ChevronRight, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun HistoryDetail(
    padding: PaddingValues,
    entry: InstallHistoryEntry,
    sharedScope: SharedTransitionScope,
    animatedScope: androidx.compose.animation.AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val exportLogLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        result.data?.data?.let { uri -> saveRunLog(context, uri, entry) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).hudScanlines().hudVignette(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                HudHeader(left = "LOG", right = "DETAIL")
            Row(
                modifier = Modifier.padding(top = 12.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconButton(onClick = {
                    clickHaptic(view)
                    onBack()
                }) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.action_back))
                }
                Text(
                    stringResource(R.string.history_detail_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = HudColors.Bone,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = {
                    clickHaptic(view)
                    exportLogLauncher.launch(
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TITLE, runLogFileName(entry))
                        },
                    )
                }) {
                    Icon(Icons.Rounded.Save, contentDescription = stringResource(R.string.export_log))
                }
            }
            }
        }
        item {
            with(sharedScope) {
                HistoryResultCard(
                    entry = entry,
                    heroModifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "history-${entry.id}"),
                        animatedVisibilityScope = animatedScope,
                    ),
                )
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = hudCutShape(12.dp),
                border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(
                    containerColor = HudColors.Terminal,
                    contentColor = HudColors.Bone,
                ),
            ) {
                Text(
                    text = entry.log.ifBlank { stringResource(R.string.history_log_empty) },
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = HudColors.Bone,
                )
            }
        }
    }
}

@Composable
private fun HistoryResultCard(entry: InstallHistoryEntry, heroModifier: Modifier = Modifier) {
    val containerColor = historyResultContainerColor(entry.result)
    val contentColor = historyResultContentColor(entry.result)
    Card(
        modifier = heroModifier.fillMaxWidth(),
        shape = hudCutShape(12.dp),
        border = BorderStroke(1.dp, HudColors.Blood.copy(alpha = 0.45f)),
        colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(historyResultIcon(entry.result), contentDescription = null, modifier = Modifier.size(38.dp))
            Column {
                Text(historyResultLabel(entry.result), style = MaterialTheme.typography.titleLarge)
                Text(
                    stringResource(R.string.history_started, formatHistoryTime(entry.startedAtMillis)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.78f),
                )
                entry.completedAtMillis?.let { completedAt ->
                    Text(
                        stringResource(R.string.history_completed, formatHistoryTime(completedAt)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.78f),
                    )
                }
                entry.profileId?.let { profileId ->
                    Text(
                        stringResource(R.string.history_payload, profileId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.78f),
                    )
                }
                Text(
                    stringResource(
                        if (entry.usedShizuku) {
                            R.string.history_shizuku_used
                        } else {
                            R.string.history_shizuku_not_used
                        },
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.78f),
                )
            }
        }
    }
}

@Composable
private fun historyResultLabel(result: InstallRunResult): String = stringResource(
    when (result) {
        InstallRunResult.Running -> R.string.history_running
        InstallRunResult.Succeeded -> R.string.history_succeeded
        InstallRunResult.Failed -> R.string.history_failed
    },
)

private fun historyResultIcon(result: InstallRunResult): ImageVector = when (result) {
    InstallRunResult.Running -> Icons.Rounded.Schedule
    InstallRunResult.Succeeded -> Icons.Rounded.CheckCircle
    InstallRunResult.Failed -> Icons.Rounded.Error
}

@Composable
private fun historyResultContainerColor(result: InstallRunResult): Color = when (result) {
    InstallRunResult.Running -> HudColors.PlateHigh
    InstallRunResult.Succeeded -> HudColors.DarkRed
    InstallRunResult.Failed -> Color(0xFF2A0E0E)
}

@Composable
private fun historyResultContentColor(result: InstallRunResult): Color = HudColors.Bone

@Composable
private fun formatHistoryTime(timestamp: Long): String {
    val locale = LocalConfiguration.current.locales[0]
    return remember(timestamp, locale) {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale)
            .format(Date(timestamp))
    }
}

private fun runLogFileName(entry: InstallHistoryEntry): String =
    "RootMyGalaxy-" +
        SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date(entry.startedAtMillis)) +
        "-${entry.result.name.lowercase(Locale.US)}.log"

private fun saveRunLog(context: Context, uri: Uri, entry: InstallHistoryEntry) {
    val content = entry.log.ifBlank { context.getString(R.string.history_log_empty) }
    val saved = runCatching {
        context.contentResolver.openOutputStream(uri)?.use { output ->
            output.write(content.toByteArray(Charsets.UTF_8))
        } ?: error("open failed")
        true
    }.getOrDefault(false)
    Toast.makeText(
        context,
        if (saved) {
            context.getString(R.string.export_log_saved)
        } else {
            context.getString(R.string.export_log_failed)
        },
        Toast.LENGTH_LONG,
    ).show()
}

@Composable
private fun SettingsPage(
    padding: PaddingValues,
    accentColor: AccentColor,
    themeMode: AppThemeMode,
    advancedMode: Boolean,
    shizukuMode: Boolean,
    rootActive: Boolean,
    restartZygoteAfterRoot: Boolean,
    autoStartShizukuAfterRoot: Boolean,
    soundEnabled: Boolean,
    onSoundChanged: (Boolean) -> Unit,
    updateStatus: UpdateStatus,
    onCheckForUpdate: () -> Unit,
    onStartDownload: (UpdateInfo) -> Unit,
    onAccentColorChanged: (AccentColor) -> Unit,
    onThemeModeChanged: (AppThemeMode) -> Unit,
    onAdvancedModeChanged: (Boolean) -> Unit,
    onShizukuModeChanged: (Boolean) -> Unit,
    onRestartZygoteChanged: (Boolean) -> Unit,
    onAutoStartShizukuChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    var showUptimeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showShizukuMissingDialog by remember { mutableStateOf(false) }
    var languageMenuTop by remember { mutableStateOf(32.dp) }
    var colorMenuTop by remember { mutableStateOf(32.dp) }
    var uptimeMenuTop by remember { mutableStateOf(32.dp) }
    var uptimeSeconds by remember { mutableStateOf(AppPreferences.manualBootMinUptimeSeconds(context)) }
    val density = LocalDensity.current
    val currentLanguageTag = AppPreferences.languageTag(context)

    if (showShizukuMissingDialog) {
        AlertDialog(
            onDismissRequest = { showShizukuMissingDialog = false },
            icon = { Icon(Icons.Rounded.Info, contentDescription = null) },
            title = {
                DialogDimAmount(0.34f)
                Text(stringResource(R.string.shizuku_not_running_title))
            },
            text = { Text(stringResource(R.string.shizuku_not_running_body)) },
            confirmButton = {
                FilledTonalButton(onClick = {
                    clickHaptic(view)
                    showShizukuMissingDialog = false
                    openShizukuManager(context)
                }) {
                    Text(stringResource(R.string.action_download_shizuku))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    clickHaptic(view)
                    showShizukuMissingDialog = false
                }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }

    if (showLanguageDialog) {
        SideChoiceMenu(
            choices = languageOptions.map { stringResource(it.label) },
            selectedIndex = languageOptions.indexOfFirst { languageMatches(it, currentLanguageTag) }
                .coerceAtLeast(0),
            topOffset = languageMenuTop,
            onSelected = { index ->
                showLanguageDialog = false
                AppPreferences.setLanguage(context, languageOptions[index].tag)
            },
            onDismiss = { showLanguageDialog = false },
        )
    }

    if (showColorDialog) {
        val colors = AccentColor.entries
        SideChoiceMenu(
            choices = colors.map { accentLabel(it) },
            selectedIndex = colors.indexOf(accentColor),
            topOffset = colorMenuTop,
            onSelected = { index ->
                showColorDialog = false
                onAccentColorChanged(colors[index])
            },
            onDismiss = { showColorDialog = false },
        )
    }

    if (showUptimeDialog) {
        val options = DiagnosticUptime.allowedSeconds
        SideChoiceMenu(
            choices = options.map { uptimeLabel(it) },
            selectedIndex = options.indexOf(uptimeSeconds).coerceAtLeast(0),
            topOffset = uptimeMenuTop,
            onSelected = { index ->
                showUptimeDialog = false
                uptimeSeconds = options[index]
                AppPreferences.setManualBootMinUptimeSeconds(context, options[index])
            },
            onDismiss = { showUptimeDialog = false },
        )
    }

    if (showAboutDialog) {
        AboutDialog(onDismiss = { showAboutDialog = false })
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).hudScanlines().hudVignette(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                HudHeader(left = "CONFIG", right = "TERMINAL")
                Text(
                    stringResource(R.string.settings),
                    style = MaterialTheme.typography.headlineLarge,
                    color = HudColors.Bone,
                )
                AppVersionText(
                    style = MaterialTheme.typography.bodyLarge,
                    color = HudColors.Steel.copy(alpha = 0.8f),
                )
            }
        }
        item { SectionLabel(stringResource(R.string.appearance)) }
        item {
            ThemeModeSelector(themeMode, onThemeModeChanged)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                SettingsCard(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        colorMenuTop = with(density) { coordinates.positionInWindow().y.toDp() }
                    },
                    icon = Icons.Rounded.Palette,
                    title = stringResource(R.string.material_color),
                    description = stringResource(R.string.material_color_description),
                    value = accentLabel(accentColor),
                    position = SettingsCardPosition.Top,
                    onClick = {
                        clickHaptic(view)
                        showColorDialog = true
                    },
                )
                SettingsCard(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        languageMenuTop = with(density) { coordinates.positionInWindow().y.toDp() }
                    },
                    icon = Icons.Rounded.Language,
                    title = stringResource(R.string.language),
                    description = stringResource(R.string.language_description),
                    value = languageLabel(currentLanguageTag),
                    position = SettingsCardPosition.Middle,
                    onClick = {
                        clickHaptic(view)
                        showLanguageDialog = true
                    },
                )
                SettingsSwitchCard(
                    icon = Icons.Rounded.VerifiedUser,
                    title = stringResource(R.string.shizuku_mode),
                    description = stringResource(R.string.shizuku_mode_description),
                    checked = shizukuMode,
                    position = SettingsCardPosition.Bottom,
                    onCheckedChange = { enabled ->
                        clickHaptic(view)
                        if (!enabled) {
                            onShizukuModeChanged(false)
                        } else {
                            scope.launch {
                                ShizukuController.pingUntilRunning()
                                if (ShizukuController.isRunning()) {
                                    onShizukuModeChanged(true)
                                    if (!ShizukuController.isGranted()) {
                                        ShizukuController.requestPermission()
                                    }
                                } else {
                                    showShizukuMissingDialog = true
                                }
                            }
                        }
                    },
                )
            }
        }
        item { SectionLabel(stringResource(R.string.advanced)) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                SettingsSwitchCard(
                    icon = Icons.Rounded.Memory,
                    title = stringResource(R.string.advanced_mode),
                    description = stringResource(R.string.advanced_mode_description),
                    checked = advancedMode,
                    position = SettingsCardPosition.Top,
                    onCheckedChange = {
                        clickHaptic(view)
                        onAdvancedModeChanged(it)
                    },
                )
                SettingsCard(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        uptimeMenuTop = with(density) { coordinates.positionInWindow().y.toDp() }
                    },
                    icon = Icons.Rounded.Schedule,
                    title = stringResource(R.string.boot_min_uptime),
                    description = stringResource(R.string.boot_min_uptime_description),
                    value = uptimeLabel(uptimeSeconds),
                    position = SettingsCardPosition.Bottom,
                    onClick = {
                        clickHaptic(view)
                        showUptimeDialog = true
                    },
                )
            }
        }
        item { SectionLabel(stringResource(R.string.shizuku_boot_section)) }
        item { ShizukuBootSettingsCard() }
        item { SectionLabel(stringResource(R.string.postroot_section)) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                SettingsSwitchCard(
                    icon = Icons.Rounded.Refresh,
                    title = stringResource(R.string.restart_zygote_after_root_title),
                    description = stringResource(R.string.restart_zygote_after_root_description),
                    checked = restartZygoteAfterRoot,
                    position = SettingsCardPosition.Top,
                    onCheckedChange = {
                        clickHaptic(view)
                        onRestartZygoteChanged(it)
                    },
                )
                SettingsSwitchCard(
                    icon = Icons.Rounded.CheckCircle,
                    title = stringResource(R.string.postroot_start_shizuku),
                    description = stringResource(R.string.postroot_start_shizuku_summary),
                    checked = autoStartShizukuAfterRoot,
                    position = SettingsCardPosition.Bottom,
                    onCheckedChange = {
                        clickHaptic(view)
                        onAutoStartShizukuChanged(it)
                    },
                )
            }
        }
        item {
            AdvancedRecoverySettings(
                rootActive = rootActive,
                autoRootEnabled = AppPreferences.autoRootEnabled(context),
                onAutoRootEnabledChanged = {},
            )
        }
        item { SectionLabel(stringResource(R.string.feedback)) }
        item {
            val sound = rememberHudSound()
            SettingsSwitchCard(
                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                title = stringResource(R.string.sound_effects),
                description = stringResource(R.string.sound_effects_description),
                checked = soundEnabled,
                onCheckedChange = {
                    clickHaptic(view)
                    onSoundChanged(it)
                    if (it) sound.play(HudSfx.Confirm, true)
                },
            )
        }
        item { SectionLabel(stringResource(R.string.about)) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                UpdateSettingsCard(
                    status = updateStatus,
                    position = SettingsCardPosition.Top,
                    onCheckForUpdate = onCheckForUpdate,
                    onStartDownload = onStartDownload,
                )
                SettingsCard(
                    icon = Icons.Rounded.Info,
                    title = stringResource(R.string.about),
                    description = stringResource(R.string.about_description),
                    value = "",
                    position = SettingsCardPosition.Bottom,
                    onClick = {
                        clickHaptic(view)
                        showAboutDialog = true
                    },
                )
            }
        }
    }
}

@Composable
private fun UpdateSettingsCard(
    status: UpdateStatus,
    position: SettingsCardPosition,
    onCheckForUpdate: () -> Unit,
    onStartDownload: (UpdateInfo) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val view = LocalView.current
    val busy = status.busy
    Card(
        onClick = {
            clickHaptic(view)
            when {
                busy -> Unit
                status is UpdateStatus.Available -> onStartDownload(status.info)
                else -> onCheckForUpdate()
            }
        },
        modifier = Modifier.fillMaxWidth().hudPressScale(interactionSource),
        shape = hudCutShape(10.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.4f)),
        interactionSource = interactionSource,
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when {
                status is UpdateStatus.Checking -> LoadingIndicator(
                    modifier = Modifier.size(28.dp),
                    color = HudColors.Blood,
                )
                status is UpdateStatus.Downloading -> CircularProgressIndicator(
                    progress = { status.progress },
                    modifier = Modifier.size(28.dp),
                    color = HudColors.Blood,
                )
                else -> Icon(
                    Icons.Rounded.SystemUpdate,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = HudColors.Blood,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (status) {
                        is UpdateStatus.Available, is UpdateStatus.Downloading ->
                            stringResource(R.string.updater_available_title)
                        else -> stringResource(R.string.updater_check)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = HudColors.Bone,
                )
                Text(
                    text = when {
                        status is UpdateStatus.Downloading -> stringResource(R.string.updater_downloading)
                        status is UpdateStatus.Checking -> stringResource(R.string.updater_checking)
                        status is UpdateStatus.Available ->
                            stringResource(R.string.updater_available_body_short, status.info.versionName)
                        status is UpdateStatus.UpToDate -> stringResource(R.string.updater_up_to_date)
                        status is UpdateStatus.Failed -> stringResource(R.string.updater_failed)
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = HudColors.BoneDim,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (status is UpdateStatus.Available) {
                Text(
                    text = stringResource(R.string.updater_button_download),
                    style = MaterialTheme.typography.labelLarge,
                    color = HudColors.Blood,
                    maxLines = 1,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TargetSelectionSheet(
    device: DeviceSnapshot,
    catalog: TargetCatalogUiState,
    hazeState: HazeState,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    onNext: (TargetProfile) -> Unit,
) {
    var showOnlyMyDevice by remember { mutableStateOf(true) }
    var selectedProfileId by remember { mutableStateOf<String?>(null) }
    val view = LocalView.current
    val visibleProfiles = remember(catalog.profiles, showOnlyMyDevice, device) {
        if (showOnlyMyDevice) {
            catalog.profiles.filter { it.matches(device) }
        } else {
            catalog.profiles
        }
    }
    val selectedProfile = catalog.profiles.firstOrNull { it.profileId == selectedProfileId }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.hazeEffect(
            hazeState,
            style = HazeStyle(
                backgroundColor = HudColors.PlateHigh.copy(alpha = 0.88f),
                blurRadius = 18.dp,
                tints = emptyList(),
            ),
        ),
        containerColor = HudColors.PlateHigh,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                HudHeader(left = "TARGET", right = "SELECT")
                Text(
                    stringResource(R.string.select_device_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = HudColors.Bone,
                )
                Text(
                    stringResource(R.string.select_device_description),
                    color = HudColors.BoneDim,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = showOnlyMyDevice,
                        role = Role.Checkbox,
                        onValueChange = { enabled ->
                            clickHaptic(view)
                            showOnlyMyDevice = enabled
                            if (enabled && selectedProfile?.matches(device) == false) {
                                selectedProfileId = null
                            }
                        },
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Checkbox(checked = showOnlyMyDevice, onCheckedChange = null)
                Text(stringResource(R.string.show_my_device_only), style = MaterialTheme.typography.titleMedium)
            }

            when {
                catalog.loading -> Box(
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    LoadingIndicator(color = HudColors.Blood)
                }
                catalog.error != null -> Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(catalog.error, color = HudColors.WarningAmber)
                    FilledTonalButton(onClick = onRetry) {
                        Text(stringResource(R.string.action_retry))
                    }
                }
                visibleProfiles.isEmpty() -> Text(
                    stringResource(R.string.no_matching_devices),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                    color = HudColors.BoneDim,
                )
                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 480.dp)
                        .selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(visibleProfiles, key = TargetProfile::profileId) { profile ->
                        val selected = selectedProfileId == profile.profileId
                        val matchingModel = profile.models.firstOrNull {
                            it.equals(device.model, ignoreCase = true)
                        }
                        val modelLabel = matchingModel ?: profile.models.take(3).joinToString().let {
                            if (profile.models.size > 3) "$it +${profile.models.size - 3}" else it
                        }
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = hudCutShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                if (selected) HudColors.Blood else HudColors.SteelDim.copy(alpha = 0.4f),
                            ),
                            color = if (selected) {
                                HudColors.DarkRed
                            } else {
                                HudColors.Plate
                            },
                            contentColor = HudColors.Bone,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = selected,
                                        role = Role.RadioButton,
                                        onClick = {
                                            clickHaptic(view)
                                            selectedProfileId = profile.profileId
                                        },
                                    )
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                RadioButton(selected = selected, onClick = null)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        profile.displayName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = HudColors.Bone,
                                    )
                                    Text(
                                        modelLabel,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = HudColors.BoneDim,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TextButton(onClick = {
                    clickHaptic(view)
                    onDismiss()
                }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.action_cancel))
                }
                Button(
                    onClick = {
                        clickHaptic(view)
                        selectedProfile?.let(onNext)
                    },
                    enabled = selectedProfile != null,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.action_next))
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    HudSectionLabel(text = text)
}

private enum class SettingsCardPosition {
    Single,
    GroupedSingle,
    Top,
    Middle,
    Bottom,
}

@Composable
private fun SettingsCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    value: String,
    position: SettingsCardPosition = SettingsCardPosition.Single,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val view = LocalView.current
    Card(
        onClick = {
            clickHaptic(view)
            onClick()
        },
        modifier = modifier.fillMaxWidth().hudPressScale(interactionSource),
        shape = hudCutShape(10.dp),
        border = BorderStroke(1.dp, HudColors.SteelDim.copy(alpha = 0.4f)),
        interactionSource = interactionSource,
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = HudColors.Blood)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = HudColors.Bone)
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HudColors.BoneDim,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                value,
                style = MaterialTheme.typography.labelLarge,
                color = HudColors.Blood,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun SettingsSwitchCard(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    position: SettingsCardPosition = SettingsCardPosition.Single,
    onCheckedChange: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val view = LocalView.current
    Card(
        onClick = {
            clickHaptic(view)
            onCheckedChange(!checked)
        },
        modifier = Modifier.fillMaxWidth().hudPressScale(interactionSource),
        shape = hudCutShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (checked) HudColors.Blood.copy(alpha = 0.6f)
            else HudColors.SteelDim.copy(alpha = 0.4f),
        ),
        interactionSource = interactionSource,
        colors = CardDefaults.cardColors(
            containerColor = HudColors.Plate,
            contentColor = HudColors.Bone,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = HudColors.Blood)
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    HudLed(color = if (checked) HudColors.Blood else HudColors.SteelDim)
                    Text(title, style = MaterialTheme.typography.titleMedium, color = HudColors.Bone)
                }
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HudColors.BoneDim,
                )
            }
            Switch(checked = checked, onCheckedChange = null)
        }
    }
}

@Composable
private fun ThemeModeSelector(
    themeMode: AppThemeMode,
    onThemeModeChanged: (AppThemeMode) -> Unit,
) {
    val view = LocalView.current
    val themeModes = AppThemeMode.entries
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        themeModes.forEachIndexed { index, mode ->
            ToggleButton(
                checked = themeMode == mode,
                onCheckedChange = {
                    clickHaptic(view)
                    onThemeModeChanged(mode)
                },
                modifier = Modifier.weight(1f).semantics { role = Role.RadioButton },
                colors = ToggleButtonDefaults.toggleButtonColors(
                    containerColor = HudColors.Plate,
                    checkedContainerColor = HudColors.DarkRed,
                    checkedContentColor = HudColors.Bone,
                ),
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    themeModes.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                contentPadding = PaddingValues(horizontal = 10.dp),
            ) {
                Icon(
                    imageVector = when (mode) {
                        AppThemeMode.System -> Icons.Rounded.BrightnessAuto
                        AppThemeMode.Light -> Icons.Rounded.LightMode
                        AppThemeMode.Dark -> Icons.Rounded.DarkMode
                    },
                    contentDescription = null,
                )
                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                Text(themeModeLabel(mode), maxLines = 1)
            }
        }
    }
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val view = LocalView.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogDimAmount(0.34f)
            Text(stringResource(R.string.about_title))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(stringResource(R.string.about_body))
                AppVersionText(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider()
                Surface(
                    onClick = {
                        clickHaptic(view)
                        uriHandler.openUri(KERNEL_SU_HOME_URL)
                    },
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(painterResource(R.drawable.ic_kernelsu), contentDescription = null)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.kernelsu_card_title),
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                stringResource(R.string.kernelsu_card_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Icon(Icons.Rounded.Link, contentDescription = stringResource(R.string.open_github))
                    }
                }
                Surface(
                    onClick = {
                        clickHaptic(view)
                        uriHandler.openUri(ROOT_MY_GALAXY_URL)
                    },
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(painterResource(R.drawable.ic_github), contentDescription = null)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.github_card_title),
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                stringResource(R.string.github_card_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Icon(Icons.Rounded.Link, contentDescription = stringResource(R.string.open_github))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                clickHaptic(view)
                onDismiss()
            }) {
                Text(stringResource(R.string.action_close))
            }
        },
    )
}

@Composable
private fun expressiveClickableCardShape(
    interactionSource: MutableInteractionSource,
    position: SettingsCardPosition = SettingsCardPosition.Single,
): androidx.compose.ui.graphics.Shape {
    // HUD angular variant: same pressed/spring behavior, chamfered cut instead of round.
    // Position param kept for API stability; cut depth varies slightly by group.
    val pressed by interactionSource.collectIsPressedAsState()
    val cut by animateDpAsState(
        targetValue = when {
            pressed -> 18.dp
            position == SettingsCardPosition.Single -> 12.dp
            position in setOf(SettingsCardPosition.GroupedSingle, SettingsCardPosition.Top) -> 14.dp
            position in setOf(SettingsCardPosition.GroupedSingle, SettingsCardPosition.Bottom) -> 14.dp
            else -> 8.dp
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "clickable-card-cut",
    )
    return hudCutShape(cut)
}

@Composable
private fun SideChoiceMenu(
    choices: List<String>,
    selectedIndex: Int,
    topOffset: Dp,
    onSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    var closing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val scrimAlpha by animateFloatAsState(
        targetValue = if (visible) 0.34f else 0f,
        animationSpec = tween(durationMillis = if (visible) 160 else 180),
        label = "menu-scrim",
    )

    fun closeMenu(afterAnimation: () -> Unit) {
        if (closing) return
        closing = true
        visible = false
        coroutineScope.launch {
            delay(MENU_EXIT_WAIT_MILLIS)
            afterAnimation()
        }
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    Popup(
        onDismissRequest = { closeMenu(onDismiss) },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            clippingEnabled = false,
        ),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val estimatedHeight = 16.dp + 56.dp * choices.size
            val constrainedTop = minOf(
                topOffset,
                maxHeight - estimatedHeight - 24.dp,
            ).coerceAtLeast(16.dp)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = scrimAlpha))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { closeMenu(onDismiss) },
                    ),
            )
            AnimatedVisibility(
                visible = visible,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = constrainedTop, end = 18.dp),
                enter = scaleIn(
                    animationSpec = keyframes {
                        durationMillis = 200
                        1.025f at 95
                        0.995f at 155
                    },
                    initialScale = 0.94f,
                    transformOrigin = TransformOrigin(1f, 0f),
                ),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = MENU_EXIT_ANIMATION_MILLIS),
                    targetScale = 0.86f,
                    transformOrigin = TransformOrigin(1f, 0.5f),
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 160,
                        delayMillis = 20,
                    ),
                ),
            ) {
                Surface(
                    modifier = Modifier
                        .width(196.dp)
                        .heightIn(max = 620.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ),
                    shape = hudCutShape(10.dp),
                    border = BorderStroke(1.dp, HudColors.Blood.copy(alpha = 0.5f)),
                    color = HudColors.PlateHigh,
                    contentColor = HudColors.Bone,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        itemsIndexed(choices) { index, choice ->
                            val selected = index == selectedIndex
                            Surface(
                                onClick = {
                                    clickHaptic(view)
                                    closeMenu { onSelected(index) }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = hudCutShape(8.dp),
                                color = if (selected) {
                                    HudColors.DarkRed
                                } else {
                                    Color.Transparent
                                },
                                contentColor = HudColors.Bone,
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    if (selected) {
                                        Icon(
                                            Icons.Rounded.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(22.dp),
                                        )
                                    }
                                    Text(
                                        text = choice,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val MENU_EXIT_ANIMATION_MILLIS = 180
private const val MENU_EXIT_WAIT_MILLIS = 200L

@Composable
private fun languageLabel(tag: String): String =
    languageOptions.firstOrNull { languageMatches(it, tag) }
        ?.let { stringResource(it.label) }
        ?: stringResource(R.string.language_system)

private fun languageMatches(option: LanguageOption, currentTag: String): Boolean {
    if (option.tag.isEmpty()) return currentTag.isEmpty()
    return currentTag == option.tag || currentTag.startsWith("$option.tag-")
}

@Composable
private fun accentLabel(color: AccentColor): String = when (color) {
    AccentColor.Dynamic -> stringResource(R.string.color_dynamic)
    AccentColor.Blue -> stringResource(R.string.color_blue)
    AccentColor.Violet -> stringResource(R.string.color_violet)
    AccentColor.Green -> stringResource(R.string.color_green)
    AccentColor.Orange -> stringResource(R.string.color_orange)
}

@Composable
private fun themeModeLabel(themeMode: AppThemeMode): String = when (themeMode) {
    AppThemeMode.System -> stringResource(R.string.theme_system)
    AppThemeMode.Light -> stringResource(R.string.theme_light)
    AppThemeMode.Dark -> stringResource(R.string.theme_dark)
}

@Composable
private fun uptimeLabel(seconds: Int): String =
    if (seconds <= 0) {
        stringResource(R.string.uptime_off)
    } else {
        stringResource(R.string.uptime_seconds_format, seconds)
    }
