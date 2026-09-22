package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * ULTRAKILL boot intro: MANKIND IS DEAD / BLOOD IS FUEL / HELL IS FULL / ROOT IS...? — with skip.
 * Pure Compose, ~2.6s, skippable on tap. Shown once (AppPreferences).
 */
@Composable
fun HudBootIntro(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var line by remember { mutableStateOf(0) }
    var skipped by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(500)
        line = 1
        delay(650)
        line = 2
        delay(650)
        line = 3
        delay(700)
        if (!skipped) onDone()
    }
    fun skip() {
        skipped = true
        onDone()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HudColors.Void)
            .hudAnimatedScanlines(enabled = true, alpha = 0.09f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = ::skip,
            )
            .padding(28.dp),
        contentAlignment = Alignment.Center,
    ) {
        RitualRing(
            spinning = true,
            modifier = Modifier.size(240.dp).align(Alignment.Center),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            HudHeader(left = "HELL", right = "BOOT")
            BootLine(visible = line >= 1, text = "MANKIND IS DEAD.")
            BootLine(visible = line >= 2, text = "BLOOD IS FUEL.")
            BootLine(visible = line >= 3, text = "HELL IS FULL.", accent = true)
            if (line >= 3) {
                Text(
                    text = "// tap to skip >>",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = HudColors.Steel.copy(alpha = 0.7f),
                )
            }
        }
        TextButton(
            onClick = ::skip,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
        ) {
            Text(
                "SKIP >>",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = HudColors.Blood,
            )
        }
    }
}

@Composable
private fun BootLine(visible: Boolean, text: String, accent: Boolean = false) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(280)),
        exit = fadeOut(),
    ) {
        if (visible) {
            HudTypewriter(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    letterSpacing = 1.sp,
                ),
                color = if (accent) HudColors.Blood else HudColors.Bone,
                charsPerSecond = 60,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text("")
        }
    }
}
