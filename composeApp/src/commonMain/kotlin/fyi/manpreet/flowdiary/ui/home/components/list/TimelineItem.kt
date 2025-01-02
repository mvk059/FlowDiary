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
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_excited
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimelineItem(
    modifier: Modifier = Modifier,
    isLastItem: Boolean,
    content: @Composable (modifier: Modifier) -> Unit
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
                    color = Color.LightGray,
                    start = Offset(
                        x = circleRadiusInPxWithPadding,
                        y = (circleRadiusInPx * 2) + 8.dp.toPx()
                    ),
                    end = Offset(
                        x = circleRadiusInPxWithPadding,
                        y = this.size.height + 8.dp.toPx()
                    ),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {

        Row {

            Icon(
                painter = painterResource(Res.drawable.ic_excited),
                contentDescription = null,
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.small,
                    start = MaterialTheme.spacing.small
                ),
                tint = Color.Unspecified,
            )

            content(
                Modifier.padding(
                    start = MaterialTheme.spacing.small,
                    end = MaterialTheme.spacing.medium,
                    bottom = if (isLastItem.not()) MaterialTheme.spacing.medium else 0.dp
                )
            )
        }
    }
}
