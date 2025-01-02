package fyi.manpreet.flowdiary.ui.home.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import fyi.manpreet.flowdiary.ui.home.components.chips.TopicChip
import fyi.manpreet.flowdiary.ui.theme.spacing

@Composable
fun AudioEntryContentItem(
    modifier: Modifier = Modifier
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
                    text = "My Entry",
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
            ExpandableText(text = sampleText)

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                TopicChip(topic = "Work")
                TopicChip(topic = "Office")
            }
        }
    }
}