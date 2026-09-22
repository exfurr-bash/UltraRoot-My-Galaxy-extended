package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

/**
 * Terminal typewriter: reveals [text] at [charsPerSecond]. Cheap, no per-frame Canvas cost.
 * When text grows (live log tail), it animates only the tail.
 */
@Composable
fun HudTypewriter(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    charsPerSecond: Int = 90,
    enabled: Boolean = true,
) {
    var visibleCount by remember(text) { mutableStateOf(if (enabled) 0 else text.length) }
    LaunchedEffect(text, enabled) {
        if (!enabled) {
            visibleCount = text.length
            return@LaunchedEffect
        }
        val target = text.length
        val start = visibleCount.coerceAtMost(target)
        var i = start
        val delayMs = (1000L / charsPerSecond.coerceAtLeast(20)).coerceAtLeast(8L)
        // Fast-forward long texts: reveal in chunks so ritual never feels slow.
        val chunk = when {
            target > 600 -> 6
            target > 200 -> 3
            else -> 1
        }
        while (i < target) {
            i = (i + chunk).coerceAtMost(target)
            visibleCount = i
            delay(delayMs)
        }
    }
    Row(modifier = modifier) {
        Text(
            text = text.take(visibleCount),
            style = style,
            color = color,
        )
        if (visibleCount < text.length) {
            val transition = rememberInfiniteTransition(label = "hud-cursor")
            val alpha by transition.animateFloat(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "hud-cursor-alpha",
            )
            Text(
                text = "▌",
                style = style,
                color = HudColors.Blood.copy(alpha = alpha),
            )
        }
    }
}
