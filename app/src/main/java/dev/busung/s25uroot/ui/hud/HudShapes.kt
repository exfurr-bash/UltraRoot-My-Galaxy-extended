package dev.busung.s25uroot.ui.hud

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Industrial cut-corner (chamfered) shape. Cheap path, no shader.
 * Used as drop-in Shape for Cards / Surfaces — behavior unchanged.
 */
@Composable
fun hudCutShape(cut: Dp = 10.dp): Shape {
    val px = with(LocalDensity.current) { cut.toPx() }
    return GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        // Octagon-style chamfer, clamped so tiny components never collapse.
        val cx = px.coerceAtMost(w / 4f).coerceAtMost(h / 4f)
        moveTo(cx, 0f)
        lineTo(w - cx, 0f)
        lineTo(w, cx)
        lineTo(w, h - cx)
        lineTo(w - cx, h)
        lineTo(cx, h)
        lineTo(0f, h - cx)
        lineTo(0f, cx)
        close()
    }
}

object HudShapes {
    @Composable
    fun small(): Shape = hudCutShape(6.dp)

    @Composable
    fun medium(): Shape = hudCutShape(10.dp)

    @Composable
    fun large(): Shape = hudCutShape(14.dp)
}
