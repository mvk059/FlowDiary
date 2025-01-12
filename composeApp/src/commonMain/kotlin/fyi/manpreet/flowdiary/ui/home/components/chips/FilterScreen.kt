package fyi.manpreet.flowdiary.ui.home.components.chips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import fyi.manpreet.flowdiary.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    moodChip: FilterOption?,
    topicsChip: FilterOption?,
    onMoodChipItemSelect: (HomeEvent.Chip) -> Unit,
    onTopicChipItemSelect: (HomeEvent.Chip) -> Unit,
    onMoodChipReset: (HomeEvent.Chip) -> Unit,
    onTopicChipReset: (HomeEvent.Chip) -> Unit,
) {

    if (moodChip == null || topicsChip == null) return

    FlowRow(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ChipFilter(
            filterOption = moodChip,
            selectedOptions = moodChip.options.filter { it.isSelected },
            shouldShowIcon = true,
            onOptionSelected = { onMoodChipItemSelect(HomeEvent.Chip.MoodChip(it)) },
            onReset = { onMoodChipReset(HomeEvent.Chip.MoodReset) }
        )

        ChipFilter(
            filterOption = topicsChip,
            selectedOptions = topicsChip.options.filter { it.isSelected },
            shouldShowIcon = false,
            onOptionSelected = { onTopicChipItemSelect(HomeEvent.Chip.TopicChip(it)) },
            onReset = { onTopicChipReset(HomeEvent.Chip.TopicReset) }
        )
    }
}