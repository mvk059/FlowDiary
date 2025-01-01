package fyi.manpreet.flowdiary.ui.home.state

import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption

sealed interface HomeEvent {

    sealed interface Chip : HomeEvent {
        data class MoodChip(val id: Int) : Chip
        data class TopicChip(val id: Int) : Chip
        data object MoodReset : Chip
        data object TopicReset : Chip
    }
}
