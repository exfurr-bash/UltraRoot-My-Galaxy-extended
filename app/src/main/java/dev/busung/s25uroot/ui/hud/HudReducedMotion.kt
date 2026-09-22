package dev.busung.s25uroot.ui.hud

import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * True when the user disabled animations at the OS level
 * (Settings → Accessibility → Remove animations, i.e. animator duration
 * scale 0). Infinite HUD effects (cursor blink, scanlines, rings, Lottie
 * loops, blood pulse, LED blink, rank pop) should render a static frame
 * instead of animating forever.
 */
@Composable
fun animationsDisabled(): Boolean {
    val context = LocalContext.current
    return remember {
        runCatching {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            ) == 0f
        }.getOrDefault(false)
    }
}

/** Non-composable variant for services / Canvas code with a Context. */
fun animationsDisabled(context: Context): Boolean = runCatching {
    Settings.Global.getFloat(
        context.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f,
    ) == 0f
}.getOrDefault(false)
