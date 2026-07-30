package com.explorefaraya.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.explorefaraya.app.ui.theme.FBGold
import com.explorefaraya.app.ui.theme.FBSerif

/** Thin single-line mountain mark, recreated in the gold brand color. */
@Composable
fun FarayaMountainMark(modifier: Modifier = Modifier, size: Dp = 36.dp, color: Color = FBGold) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = w * 0.05f
        val stroke = Stroke(width = strokeWidth, join = StrokeJoin.Miter)

        val leftPeak = Path().apply {
            moveTo(w * 0.05f, h * 0.85f)
            lineTo(w * 0.40f, h * 0.10f)
            lineTo(w * 0.62f, h * 0.85f)
        }
        drawPath(leftPeak, color = color, style = stroke)

        val rightPeak = Path().apply {
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
fun FarayaLogoHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        FarayaMountainMark()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "FARAYA & BEYOND",
            fontFamily = FBSerif,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            letterSpacing = 2.sp,
            color = FBGold
        )
    }
}
