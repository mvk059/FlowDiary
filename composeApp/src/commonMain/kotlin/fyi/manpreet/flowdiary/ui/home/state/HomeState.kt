package fyi.manpreet.flowdiary.ui.home.state

import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption

data class HomeState(
    val recordings: List<Recordings> = emptyList(),
    val moodChip: FilterOption? = null,
    val topicsChip: FilterOption? = null,
    val fabBottomSheet: HomeEvent.FabBottomSheet = HomeEvent.FabBottomSheet.SheetHide,
    val recordingPath: AudioPath? = null,
)

sealed interface Recordings {
    data class Date(val date: String) : Recordings
    data class Entry(val recordings: List<Audio>) : Recordings
}