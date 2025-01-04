package fyi.manpreet.flowdiary.ui.home.state

sealed interface HomeEvent {

    sealed interface Chip : HomeEvent {
        data class MoodChip(val id: Int) : Chip
        data class TopicChip(val id: Int) : Chip
        data object MoodReset : Chip
        data object TopicReset : Chip
    }

    data object FabClick : HomeEvent

    sealed interface AudioPlayer : HomeEvent {
        data object Play : AudioPlayer
        data object Pause : AudioPlayer
    }

    sealed interface Permission : HomeEvent {
        data class Settings(val type: fyi.manpreet.flowdiary.platform.permission.Permission) : Permission
        data object Close : Permission
    }
}
