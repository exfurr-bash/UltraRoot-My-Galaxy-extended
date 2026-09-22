package dev.busung.s25uroot.ui.hud

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.busung.s25uroot.R

/**
 * Lottie wrappers with pure-Canvas fallback. If the composition fails to load,
 * the Canvas ritual ring takes over so the screen never looks empty.
 */

@Composable
fun BloodBurstLottie(
    burstKey: Any?,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
) {
    if (burstKey == null) return
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.blood_burst))
    // Key progress on burstKey so every new burst replays from 0.
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        restartOnPlay = true,
    )
    androidx.compose.runtime.key(burstKey) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(size),
                )
            } else {
                RitualRing(spinning = true, modifier = Modifier.size(size))
            }
        }
    }
}

@Composable
fun PentagramSpinLottie(
    spinning: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 240.dp,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pentagram_spin))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = spinning,
        restartOnPlay = false,
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(size),
            )
        } else {
            RitualRing(spinning = spinning, modifier = Modifier.size(size))
        }
    }
}
