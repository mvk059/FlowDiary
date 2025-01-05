package fyi.manpreet.flowdiary.ui.home.state

import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption

data class HomeState(
    val moodChip: FilterOption? = null,
    val topicsChip: FilterOption? = null,
    val fabBottomSheet: HomeEvent.FabBottomSheet = HomeEvent.FabBottomSheet.SheetHide,
    val recordingPath: AudioPath? = null,
)
