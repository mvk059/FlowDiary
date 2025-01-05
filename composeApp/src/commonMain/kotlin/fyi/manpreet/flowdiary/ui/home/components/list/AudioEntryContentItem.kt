package fyi.manpreet.flowdiary.ui.home.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.ui.home.components.chips.TopicChip
import fyi.manpreet.flowdiary.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AudioEntryContentItem(
    modifier: Modifier = Modifier,
    recording: Audio,
) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = MaterialTheme.spacing.extraSmall,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {

        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = recording.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "17:30",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Seekbar

            val sampleText =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tit amet, consectetur adipiscing elit, sed tLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tit amet, consectetur adipiscing elit, sed t"
            ExpandableText(text = recording.description)

            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                recording.topics.forEach { topic ->
                    TopicChip(topic = topic.value)
                }
            }
        }
    }
}