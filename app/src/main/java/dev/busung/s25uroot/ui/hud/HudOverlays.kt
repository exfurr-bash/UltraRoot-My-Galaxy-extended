package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Cheap CRT scanlines: 1px lines every 5px, very low alpha.
 * Static drawBehind — no per-frame cost.
 */
fun Modifier.hudScanlines(
    alpha: Float = 0.05f,
    color: Color = Color.Black,
): Modifier = this.drawBehind {
    val step = 5.dp.toPx().coerceAtLeast(4f)
    var y = 0f
    while (y < size.height) {
        drawLine(
            color = color.copy(alpha = alpha),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f,
        )
        y += step
    }
}

/**
 * Cheap vignette: radial darkening at edges. Single gradient rect.
 */
fun Modifier.hudVignette(
    strength: Float = 0.22f,
): Modifier = this.drawBehind {
    val brush = Brush.radialGradient(
        colors = listOf(
            Color.Transparent,
            Color.Black.copy(alpha = strength),
        ),
        center = Offset(size.width / 2f, size.height / 2f),
        radius = maxOf(size.width, size.height) * 0.72f,
    )
    drawRect(brush = brush, size = size)
}

/**
 * Immediate press feedback: scale to 0.97 while pressed with stiff spring back.
 * Read-only on interactionSource — no click/gesture behavior changed.
 */
@Composable
fun Modifier.hudPressScale(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.97f,
): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "hud-press",
    )
    return this.graphicsLayer(scaleX = scale, scaleY = scale)
}
