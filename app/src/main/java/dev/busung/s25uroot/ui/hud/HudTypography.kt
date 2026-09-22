package dev.busung.s25uroot.ui.hud

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Aggressive HUD typography built only from system fonts (no binary assets).
 * Headers: monospace black, uppercase rendering done at call sites via .uppercase().
 * Body: untouched for legibility.
 */
fun hudTypography(base: Typography): Typography = base.copy(
    headlineLarge = base.headlineLarge.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.5.sp,
    ),
    headlineSmall = base.headlineSmall.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
    ),
    titleLarge = base.titleLarge.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
    ),
    titleMedium = base.titleMedium.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
    ),
    titleSmall = base.titleSmall.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = base.labelLarge.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
    ),
    labelMedium = base.labelMedium.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
    ),
)

@Composable
fun hudMonoLabel(): TextStyle = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.2.sp,
    color = HudColors.Steel,
)
