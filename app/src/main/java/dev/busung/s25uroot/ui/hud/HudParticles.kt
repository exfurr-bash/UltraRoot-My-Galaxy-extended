package dev.busung.s25uroot.ui.hud

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class BloodDrop(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    var life: Float,
    var color: Color,
)

/**
 * Blood burst particles in pure Canvas — no assets, no Lottie needed for this layer.
 * One-shot: increases [burstKey] to explode again (success / failure).
 */
@Composable
fun BloodParticles(
    burstKey: Any?,
    modifier: Modifier = Modifier,
    count: Int = 42,
) {
    val safeCount = count.coerceIn(0, 64)
    var drops by remember { mutableStateOf<List<BloodDrop>>(emptyList()) }

    LaunchedEffect(burstKey) {
        if (burstKey == null) return@LaunchedEffect
        val rng = Random(burstKey.hashCode())
        drops = List(safeCount) {
            val angle = rng.nextFloat() * Math.PI.toFloat() * 2f
            val speed = 3f + rng.nextFloat() * 9f
            BloodDrop(
                x = 0.5f, y = 0.45f,
                vx = cos(angle) * speed / 100f,
                vy = sin(angle) * speed / 100f - 0.02f,
                // Density-independent: scaled by canvas size below via fraction.
                size = 0.008f + rng.nextFloat() * 0.022f,
                life = 1f,
                color = listOf(HudColors.Blood, HudColors.BloodHot, HudColors.DeepRed, HudColors.Vengeance).random(rng),
            )
        }
        repeat(40) {
            delay(32)
            drops = drops.mapNotNull { d ->
                d.vy += 0.0035f
                d.x += d.vx
                d.y += d.vy
                d.life -= 0.03f
                if (d.life > 0f && d.y < 1.1f) d else null
            }
        }
        drops = emptyList()
    }

    if (drops.isEmpty()) return
    Canvas(modifier = modifier.then(Modifier.fillMaxSize())) {
        val minSide = minOf(size.width, size.height)
        drops.forEach { d ->
            drawCircle(
                color = d.color.copy(alpha = d.life.coerceIn(0f, 1f)),
                radius = d.size * minSide * d.life.coerceAtLeast(0.2f),
                center = Offset(d.x * size.width, d.y * size.height),
            )
        }
    }
}

/**
 * Rotating pentagram / ritual ring behind boot + success states. Pure Canvas.
 */
@Composable
fun RitualRing(
    spinning: Boolean,
    modifier: Modifier = Modifier,
) {
    var angle by remember { mutableStateOf(0f) }
    val reducedMotion = animationsDisabled()
    LaunchedEffect(spinning, reducedMotion) {
        if (!spinning || reducedMotion) return@LaunchedEffect
        while (spinning) {
            delay(32)
            angle = (angle + 2.2f) % 360f
        }
    }
    Canvas(modifier = modifier) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = minOf(size.width, size.height) / 2f * 0.92f
        drawCircle(HudColors.Blood.copy(alpha = 0.5f), r, c, style = androidx.compose.ui.graphics.drawscope.Stroke(2f))
        drawCircle(HudColors.Blood.copy(alpha = 0.25f), r * 0.78f, c, style = androidx.compose.ui.graphics.drawscope.Stroke(1.5f))
        repeat(3) { i ->
            val a = Math.toRadians((angle + i * 120f).toDouble())
            val p = Offset(c.x + cos(a).toFloat() * r, c.y + sin(a).toFloat() * r)
            drawCircle(HudColors.BloodHot, 5f, p)
            drawLine(HudColors.Blood.copy(alpha = 0.6f), c, p, 1.5f)
        }
        // Inner cross slash.
        val a2 = Math.toRadians(angle.toDouble())
        drawLine(
            HudColors.Blood.copy(alpha = 0.35f),
            Offset(c.x - cos(a2).toFloat() * r * 0.7f, c.y - sin(a2).toFloat() * r * 0.7f),
            Offset(c.x + cos(a2).toFloat() * r * 0.7f, c.y + sin(a2).toFloat() * r * 0.7f),
            3f,
        )
    }
}
