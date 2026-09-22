package dev.busung.s25uroot.ui.hud

import androidx.compose.ui.graphics.Color
import dev.busung.s25uroot.AccentColor

/**
 * ULTRAKILL-inspired HUD palette. Visual only — no behavior keys changed.
 * Red is used strictly as accent; base stays black / dark gunmetal.
 */
object HudColors {
    val Blood = Color(0xFFFF1A1A)
    val Vengeance = Color(0xFFE10600)
    val Ember = Color(0xFFFF3B1F)
    val DeepRed = Color(0xFF8A0F0F)
    val DarkRed = Color(0xFF5A0A0A)

    val Void = Color(0xFF0A0A0B)
    val Gunmetal = Color(0xFF141416)
    val Plate = Color(0xFF1A1A1E)
    val PlateHigh = Color(0xFF222228)
    val Terminal = Color(0xFF050505)

    val Steel = Color(0xFF8A8F98)
    val SteelDim = Color(0xFF4A4E55)
    val Bone = Color(0xFFEDEDED)
    val BoneDim = Color(0xFFB8BCC2)

    val SuccessRed = Color(0xFFFF2B2B)
    val WarningAmber = Color(0xFFFF6B1A)

    // ULTRAKILL brutal expansion — rank + flash palette.
    val HellWhite = Color(0xFFFFF2ED)
    val BloodHot = Color(0xFFFF4040)
    val BloodDark = Color(0xFF3D0505)
    val GoldSS = Color(0xFFFFC531)
    val UltrakillYellow = Color(0xFFFFE14D)
    val DamageFlash = Color(0xFFFF1A1A)
    val GlitchCyan = Color(0xFF7DF9FF)

    val BloodGradient = listOf(Color(0xFFFF1A1A), Color(0xFF8A0F0F), Color(0xFF3D0505))
}

/**
 * Purely visual remap: every stored AccentColor renders as a red HUD variant.
 * Stored values / persistence logic untouched.
 */
fun hudSeedFor(accent: AccentColor): Color = when (accent) {
    AccentColor.Dynamic -> Color(0xFFE10600)
    AccentColor.Blue -> Color(0xFF8A0F0F)
    AccentColor.Violet -> Color(0xFF5A0A0A)
    AccentColor.Green -> Color(0xFF7A1A10)
    AccentColor.Orange -> Color(0xFFFF2B1A)
}
