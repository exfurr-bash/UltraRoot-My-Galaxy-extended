package dev.busung.s25uroot.ui.hud

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

/**
 * Centralized fast/responsive motion specs. Enters run 150-380ms
 * (see ULTRA_ENTER/RANK_POP/SHAKE); interaction-critical fades stay <220ms.
 * Brutal expansion: bouncy enters, rank pops, shakes and glitches.
 *
 * Motion-sensitive users: check [animationsDisabled] before launching
 * infinite transitions (cursor blink, scanlines, rings, Lottie loops).
 */
object HudMotion {
    const val FAST = 150
    const val MEDIUM = 200
    const val ULTRA_ENTER = 320
    const val RANK_POP = 300
    const val SHAKE = 380
    const val STAGGER = 70

    fun enter(): EnterTransition =
        fadeIn(tween(FAST)) + slideInVertically(tween(FAST)) { it / 12 }

    fun exit(): ExitTransition =
        fadeOut(tween(FAST)) + slideOutVertically(tween(FAST)) { it / 12 }

    fun brutalEnter(delay: Int = 0): EnterTransition =
        fadeIn(tween(ULTRA_ENTER, delayMillis = delay)) +
            slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
            ) { it / 6 }

    fun brutalExit(): ExitTransition =
        fadeOut(tween(MEDIUM)) + slideOutVertically(tween(MEDIUM)) { it / 8 }

    fun rankPopSpec(): FiniteAnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    fun <T> pressSpring() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    fun <T> brutalSpring() = spring<T>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow,
    )

    fun snapSpec() = tween<Float>(120)

    fun shakeSpec() = keyframes<Float> {
        durationMillis = SHAKE
        0f at 0
        -14f at 40
        12f at 100
        -9f at 160
        7f at 220
        -4f at 280
        0f at SHAKE
    }
}
