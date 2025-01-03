package fyi.manpreet.flowdiary.ui.components.emotion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastForEach
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmotionRow(
    modifier: Modifier = Modifier,
    emotions: List<Emotions>,
    onClick: (EmotionType) -> Unit,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        emotions.fastForEach { item ->
            IconButton(
                modifier = Modifier.size(MaterialTheme.spacing.large3XL),
                onClick = { onClick(item.type) },
            ) {
                Icon(
                    painter =
                    if (item.isSelected) painterResource(item.selectedIcon)
                    else painterResource(item.unselectedIcon),
                    modifier = Modifier.size(MaterialTheme.spacing.large2XL),
                    contentDescription = stringResource(item.contentDescription),
                    tint = Color.Unspecified,
                )
            }
        }
    }
}
