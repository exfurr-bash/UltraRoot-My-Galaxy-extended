package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * HUD section strip: `[ SYSTEM ] ─────── [ STATUS ]`.
 * Purely decorative; data strings passed in by callers unchanged.
 */
@Composable
fun HudHeader(
    left: String,
    right: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "[ $left ]",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = HudColors.Blood,
            maxLines = 1,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = HudColors.SteelDim.copy(alpha = 0.6f),
        )
        Text(
            text = "[ $right ]",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = HudColors.Steel,
            maxLines = 1,
        )
    }
}

/**
 * Small status LED. Blinks only when [blinking] is true (busy states).
 * Static otherwise — cheap, no continuous cost on idle screens.
 */
@Composable
fun HudLed(
    color: Color,
    blinking: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val alpha = if (blinking) {
        val transition = rememberInfiniteTransition(label = "hud-led")
        val a by transition.animateFloat(
            initialValue = 1f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(600),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "hud-led-alpha",
        )
        a
    } else {
        1f
    }
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha)),
    )
}

/**
 * Corner ticks overlay for metal-plate cards. Drawn with Canvas, 4 short L-shapes.
 */
@Composable
fun HudCornerTicks(
    color: Color = HudColors.Blood.copy(alpha = 0.85f),
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val len = 14.dp.toPx()
        val stroke = 2.dp.toPx()
        // Top-start
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, stroke / 2), androidx.compose.ui.geometry.Offset(len, stroke / 2), stroke)
        drawLine(color, androidx.compose.ui.geometry.Offset(stroke / 2, 0f), androidx.compose.ui.geometry.Offset(stroke / 2, len), stroke)
        // Top-end
        drawLine(color, androidx.compose.ui.geometry.Offset(size.width - len, stroke / 2), androidx.compose.ui.geometry.Offset(size.width, stroke / 2), stroke)
        drawLine(color, androidx.compose.ui.geometry.Offset(size.width - stroke / 2, 0f), androidx.compose.ui.geometry.Offset(size.width - stroke / 2, len), stroke)
        // Bottom-start
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, size.height - stroke / 2), androidx.compose.ui.geometry.Offset(len, size.height - stroke / 2), stroke)
        drawLine(color, androidx.compose.ui.geometry.Offset(stroke / 2, size.height - len), androidx.compose.ui.geometry.Offset(stroke / 2, size.height), stroke)
        // Bottom-end
        drawLine(color, androidx.compose.ui.geometry.Offset(size.width - len, size.height - stroke / 2), androidx.compose.ui.geometry.Offset(size.width, size.height - stroke / 2), stroke)
        drawLine(color, androidx.compose.ui.geometry.Offset(size.width - stroke / 2, size.height - len), androidx.compose.ui.geometry.Offset(size.width - stroke / 2, size.height), stroke)
    }
}

/**
 * Compact HUD section label replacing plain SectionLabel styling.
 */
@Composable
fun HudSectionLabel(text: String) {
    Text(
        text = "/// $text".uppercase(),
        style = MaterialTheme.typography.labelLarge.copy(
            fontFamily = FontFamily.Monospace,
            color = HudColors.Blood,
        ),
        modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 2.dp),
    )
}
