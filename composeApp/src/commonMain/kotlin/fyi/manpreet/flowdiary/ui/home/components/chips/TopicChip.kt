package fyi.manpreet.flowdiary.ui.home.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.hashtag_cd
import flowdiary.composeapp.generated.resources.ic_hashtag
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopicChip(
    modifier: Modifier = Modifier,
    topic: String,
) {

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large,
            )
            .height(FilterChipDefaults.Height)
            .padding(horizontal = MaterialTheme.spacing.small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            painter = painterResource(Res.drawable.ic_hashtag),
            contentDescription = stringResource(Res.string.hashtag_cd),
            tint = Color.Unspecified,
        )

        Text(
            text = topic,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}