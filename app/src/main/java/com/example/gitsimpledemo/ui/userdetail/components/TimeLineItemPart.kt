package com.example.gitsimpledemo.ui.userdetail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description: time line style item
 */

@Composable
fun TimeLineItemPart(
    position: TimelineNodePosition,
    circleParameters: CircleParameters,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 16.dp,
    spacer: Dp = 32.dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()
                drawCircle(
                    color = circleParameters.backgroundColor,
                    radius = circleRadiusInPx,
                    center = Offset(circleRadiusInPx, circleRadiusInPx)
                )
                if (lineParameters != null) {
                    drawLine(
                        brush = lineParameters.brush,
                        start = Offset(x = circleRadiusInPx, y = circleRadiusInPx * 2),
                        end = Offset(x = circleRadiusInPx, y = this.size.height),
                        strokeWidth = lineParameters.strokeWidth.toPx()
                    )
                }
            }
    ) {
        content(
            Modifier
                .padding(
                    start = contentStartOffset,
                    bottom = if (position != TimelineNodePosition.LAST) {
                        spacer
                    } else {
                        0.dp
                    }
                )
        )
    }
}

enum class TimelineNodePosition {
    FIRST,
    MIDDLE,
    LAST
}

@Preview(showBackground = true)
@Composable
private fun TimeLineItemPreview() {
}

data class CircleParameters(
    val radius: Dp,
    val backgroundColor: Color
)

data class LineParameters(
    val strokeWidth: Dp,
    val brush: Brush
)

object LineParametersDefaults {

    private val defaultLinearStrokeWidth = 3.dp

    fun linearGradient(
        strokeWidth: Dp = defaultLinearStrokeWidth,
        startColor: Color,
        endColor: Color,
        startY: Float = 0.0f,
        endY: Float = Float.POSITIVE_INFINITY
    ): LineParameters {
        val brush = Brush.verticalGradient(
            colors = listOf(startColor, endColor),
            startY = startY,
            endY = endY
        )
        return LineParameters(strokeWidth, brush)
    }
}

object CircleParametersDefaults {

    private val defaultCircleRadius = 12.dp

    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Cyan
    ) = CircleParameters(radius, backgroundColor)
}