package com.explorefaraya.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.explorefaraya.app.ui.theme.FarayaHeading

/** Recreation of the explorefaraya.com mountain mark: two overlapping outlined peaks. */
@Composable
fun FarayaMountainMark(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 40.dp, color: Color = FarayaHeading) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = w * 0.07f
        val stroke = Stroke(width = strokeWidth, join = androidx.compose.ui.graphics.StrokeJoin.Miter)

        // Left, taller peak.
        val leftPeak = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.05f, h * 0.85f)
            lineTo(w * 0.40f, h * 0.10f)
            lineTo(w * 0.62f, h * 0.85f)
        }
        drawPath(leftPeak, color = color, style = stroke)

        // Right, shorter peak, overlapping the first.
        val rightPeak = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.38f, h * 0.85f)
            lineTo(w * 0.68f, h * 0.32f)
            lineTo(w * 0.97f, h * 0.85f)
        }
        drawPath(rightPeak, color = color, style = stroke)

        drawLine(
            color = color,
            start = Offset(w * 0.02f, h * 0.85f),
            end = Offset(w * 0.98f, h * 0.85f),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun FarayaLogoHeader(modifier: Modifier = Modifier, markSize: androidx.compose.ui.unit.Dp = 56.dp) {
    Column(modifier = modifier, horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        FarayaMountainMark(size = markSize)
        Text(
            "EXPLORE",
            fontWeight = FontWeight.ExtraBold,
            fontSize = (markSize.value * 0.34f).sp,
            letterSpacing = 4.sp,
            color = FarayaHeading
        )
        Text(
            "F A R A Y A",
            fontSize = (markSize.value * 0.16f).sp,
            letterSpacing = 3.sp,
            color = FarayaHeading
        )
    }
}
