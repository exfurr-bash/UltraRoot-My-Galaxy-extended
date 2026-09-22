package dev.busung.s25uroot.ui.theme

import android.app.Activity
import android.content.Context
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme
import dev.busung.s25uroot.AccentColor
import dev.busung.s25uroot.AppThemeMode
import dev.busung.s25uroot.ui.hud.hudSeedFor
import dev.busung.s25uroot.ui.hud.hudTypography

private val AppTypography = Typography(
    displaySmall = TextStyle(fontSize = 38.sp, lineHeight = 44.sp, fontWeight = FontWeight.Light),
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Normal),
    headlineSmall = TextStyle(fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.Normal),
    titleLarge = TextStyle(fontSize = 21.sp, lineHeight = 27.sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 23.sp, fontWeight = FontWeight.Medium),
    titleSmall = TextStyle(fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 18.sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 17.sp, fontWeight = FontWeight.Medium),
)

/**
 * Visual-only seed remap. [context] kept in signature for call-site stability;
 * every stored value renders as a red HUD variant (see hudSeedFor).
 */
@Suppress("UNUSED_PARAMETER")
private fun accentSeed(context: Context, accentColor: AccentColor): Color =
    hudSeedFor(accentColor)

@Composable
fun RootMyGalaxyTheme(
    accentColor: AccentColor,
    themeMode: AppThemeMode,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    // ULTRAKILL HUD is forced dark for fidelity (approved decision).
    // themeMode preference is still persisted/read by callers; only the
    // visual output is forced dark here. No behavior change.
    @Suppress("UNUSED_VARIABLE")
    val themePreferenceKeptForApi = themeMode
    val colors = rememberDynamicColorScheme(
        seedColor = accentSeed(context, accentColor),
        isDark = true,
        style = PaletteStyle.TonalSpot,
        specVersion = ColorSpec.SpecVersion.SPEC_2025,
    ).copy(
        // Hard HUD anchors so dynamic tonal math never washes out the infernal look.
        // Surface stack forced to void/gunmetal; primary/error forced to blood red.
        surface = Color(0xFF0A0A0B),
        surfaceContainer = Color(0xFF141416),
        surfaceContainerHigh = Color(0xFF1A1A1E),
        surfaceContainerHighest = Color(0xFF222228),
        background = Color(0xFF0A0A0B),
    )

    SideEffect {
        val window = (context as Activity).window
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }

    MaterialExpressiveTheme(
        colorScheme = colors,
        typography = hudTypography(AppTypography),
        motionScheme = MotionScheme.expressive(),
        content = content,
    )
}
