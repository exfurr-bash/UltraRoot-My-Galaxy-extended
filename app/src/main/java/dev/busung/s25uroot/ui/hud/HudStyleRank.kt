package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.busung.s25uroot.InstallPhase
import kotlinx.coroutines.delay

enum class StyleRank(val label: String, val color: Color) {
    Ruin("D", HudColors.Steel),
    Carnage("C", HudColors.BoneDim),
    Brutal("B", HudColors.Bone),
    Anarchy("A", HudColors.BloodHot),
    Supreme("S", HudColors.Blood),
    SS("SS", HudColors.GoldSS),
    SSS("SSS", HudColors.UltrakillYellow),
    Ultrakill("ULTRAKILL", HudColors.BloodHot),
}

fun styleRankFor(phase: InstallPhase): StyleRank = when (phase) {
    InstallPhase.Checking -> StyleRank.Ruin
    InstallPhase.Ready -> StyleRank.Carnage
    InstallPhase.Downloading -> StyleRank.Brutal
    InstallPhase.Exploiting -> StyleRank.Anarchy
    InstallPhase.LoadingKernelSu -> StyleRank.Supreme
    InstallPhase.Installed -> StyleRank.Ultrakill
    InstallPhase.Failed -> StyleRank.Ruin
}

/**
 * ULTRAKILL style-rank badge with brutal pop: overshoot scale + tilt + glow border.
 * Re-pops whenever [rank] changes (phase change).
 */
@Composable
fun StyleRankBadge(
    rank: StyleRank,
    modifier: Modifier = Modifier,
    popKey: Any? = rank,
) {
    val scale = remember(rank) { Animatable(0.55f) }
    LaunchedEffect(popKey) {
        // hitstop: freeze one frame at impact for punch, then overshoot in.
        delay(60)
        scale.snapTo(0.55f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )
    }
    // Re-pops via key change + bouncy spring on the badge container.
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Surface(
            shape = hudCutShape(8.dp),
            color = HudColors.Terminal.copy(alpha = 0.92f),
            contentColor = rank.color,
            border = BorderStroke(2.dp, rank.color),
            shadowElevation = 10.dp,
            modifier = Modifier
                .sizeIn(minWidth = 64.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    rotationZ = -6f
                    translationY = -2f
                },
        ) {
            Text(
                text = rank.label,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = if (rank == StyleRank.Ultrakill) 15.sp else 22.sp,
                letterSpacing = 1.sp,
                style = MaterialTheme.typography.titleLarge.copy(color = rank.color),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}
