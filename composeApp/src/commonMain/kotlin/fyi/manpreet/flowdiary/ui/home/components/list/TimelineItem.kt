package fyi.manpreet.flowdiary.ui.home.components.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.getDrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimelineItem(
    modifier: Modifier = Modifier,
    emotionType: EmotionType,
    isLastItem: Boolean,
    content: @Composable (modifier: Modifier) -> Unit,
) {

    Box(
        modifier = modifier
            .drawBehind {
                val circleRadiusInPx = 12.dp.toPx()
                val padding = 8.dp.toPx()   // Keep this same as the icon top padding
                val circleRadiusInPxWithPadding = circleRadiusInPx + padding

                drawCircle(
                    color = Color.Transparent,
                    radius = circleRadiusInPx,
                    center = Offset(circleRadiusInPxWithPadding, circleRadiusInPxWithPadding)
                )
                if (isLastItem) return@drawBehind
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.7f),
                    start = Offset(
                        x = circleRadiusInPxWithPadding,
                        y = (circleRadiusInPx * 2) + 8.dp.toPx()
                    ),
                    end = Offset(
                        x = circleRadiusInPxWithPadding,
                        y = this.size.height + 12.dp.toPx()
                    ),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {

        Row {

            Icon(
                painter = painterResource(emotionType.getDrawableResource()),
                contentDescription = null,
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.smallMedium,
                    start = MaterialTheme.spacing.small
                ),
                tint = Color.Unspecified,
            )

            content(
                Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = if (isLastItem.not()) MaterialTheme.spacing.medium else 0.dp
                )
            )
        }
    }
}
