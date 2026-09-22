package dev.busung.s25uroot.ui.hud

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ULTRAKILL-style aggressive title: condensed italic black, red glow slash.
 * Built only from system fonts — no binary assets.
 */
@Composable
fun UltrakillTitle(
    text: String,
    modifier: Modifier = Modifier,
    accent: Color = HudColors.Blood,
    sub: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.5.sp,
            ),
            color = HudColors.HellWhite,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.graphicsLayer {
                rotationX = -4f
                translationY = -2f
                shadowElevation = 12f
                ambientShadowColor = accent
                spotShadowColor = accent
            },
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
        ) {
            val y = size.height / 2f
            drawLine(
                color = accent,
                start = Offset(0f, y),
                end = Offset(size.width * 0.42f, y),
                strokeWidth = 4f,
            )
            drawLine(
                color = accent.copy(alpha = 0.4f),
                start = Offset(size.width * 0.44f, y),
                end = Offset(size.width * 0.62f, y),
                strokeWidth = 2f,
            )
        }
        if (sub != null) {
            Text(
                text = sub.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = accent,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
