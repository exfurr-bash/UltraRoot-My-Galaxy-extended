package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * ULTRAKILL brutal FX. All modifiers are cheap drawBehind / graphicsLayer ops.
 * Shake is one-shot driven by a trigger key so lists don't recompose forever.
 */

@Composable
fun Modifier.hudShake(trigger: Any?, intensity: Float = 14f): Modifier {
    var offset by remember { mutableFloatStateOf(0f) }
    var lastTrigger by remember { mutableStateOf<Any?>(null) }
    val reducedMotion = animationsDisabled()
    LaunchedEffect(trigger, reducedMotion) {
        if (reducedMotion) {
            offset = 0f
            return@LaunchedEffect
        }
        if (trigger != null && trigger != lastTrigger) {
            lastTrigger = trigger
            try {
                val pattern = listOf(-1f, 0.85f, -0.65f, 0.5f, -0.3f, 0.15f, 0f)
                for (p in pattern) {
                    offset = p * intensity
                    delay(38)
                }
            } finally {
                offset = 0f
            }
        }
    }
    return this.graphicsLayer {
        translationX = offset
        translationY = offset * 0.4f
    }
}

fun Modifier.hudGlitch(intensity: Float = 0f, seed: Long = 0L): Modifier = this
    .graphicsLayer {
        if (intensity > 0.01f) {
            // Deterministic pseudo-random from seed so draws are stable per
            // frame instead of Math.random() jitter on every recomposition.
            val r1 = ((seed * 1103515245L + 12345L) ushr 16).toFloat() / 65535f - 0.5f
            val r2 = ((seed * 22695477L + 1L) ushr 16).toFloat() / 65535f - 0.5f
            translationX = r1 * 14f * intensity
            rotationX = r2 * 3f * intensity
        }
    }
    .drawBehind {
        if (intensity > 0.01f) {
            val slices = 3
            repeat(slices) { i ->
                val frac = ((seed + i * 7919L) % 1000L).toFloat() / 1000f
                val y = size.height * (0.15f + 0.3f * i + frac * 0.08f)
                drawLine(
                    color = if (i % 2 == 0) HudColors.Blood.copy(alpha = 0.35f * intensity)
                    else HudColors.GlitchCyan.copy(alpha = 0.22f * intensity),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = (2f + frac * 5f),
                )
            }
        }
    }

/** Full-screen red flash that fades. Caller drives [alpha] (e.g. phase change). */
@Composable
fun HudDamageFlash(alpha: Float, modifier: Modifier = Modifier) {
    if (alpha <= 0.01f) return
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(HudColors.DamageFlash.copy(alpha = alpha.coerceIn(0f, 0.5f)))
                val edge = Brush.radialGradient(
                    colors = listOf(Color.Transparent, HudColors.Blood.copy(alpha = alpha)),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = maxOf(size.width, size.height) * 0.6f,
                )
                drawRect(brush = edge, size = size)
            },
    )
}

/** Scanlines that drift slowly. Only use on hero / ritual screens (has per-frame cost). */
@Composable
fun Modifier.hudAnimatedScanlines(
    enabled: Boolean = true,
    alpha: Float = 0.07f,
): Modifier {
    if (!enabled || animationsDisabled()) return this.hudScanlines(alpha)
    val transition = rememberInfiniteTransition(label = "hud-scan")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Restart,
        ),
        label = "hud-scan-drift",
    )
    return this.drawBehind {
        val step = 6.dp.toPx().coerceAtLeast(4f)
        var y = -(drift % step)
        while (y < size.height) {
            drawLine(
                color = Color.Black.copy(alpha = alpha),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
            )
            y += step
        }
        drawLine(
            color = HudColors.Blood.copy(alpha = 0.10f),
            start = Offset(0f, (drift * 24f) % size.height),
            end = Offset(size.width, (drift * 24f) % size.height),
            strokeWidth = 2f,
        )
    }
}

/** Blood vignette that pulses while busy. */
@Composable
fun Modifier.hudBloodPulse(enabled: Boolean): Modifier {
    if (!enabled || animationsDisabled()) return this.hudVignette(0.28f)
    val transition = rememberInfiniteTransition(label = "hud-blood")
    val pulse by transition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.44f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "hud-blood-pulse",
    )
    return this.drawBehind {
        val brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, HudColors.Blood.copy(alpha = pulse * 0.55f), Color.Black.copy(alpha = 0.3f)),
            center = Offset(size.width / 2f, size.height / 2f),
            radius = maxOf(size.width, size.height) * 0.72f,
        )
        drawRect(brush = brush, size = size)
    }
}
