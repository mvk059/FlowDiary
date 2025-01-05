package fyi.manpreet.flowdiary.ui.home.state

sealed interface HomeEvent {

    sealed interface Chip : HomeEvent {
        data class MoodChip(val id: Int) : Chip
        data class TopicChip(val id: Int) : Chip
        data object MoodReset : Chip
        data object TopicReset : Chip
    }

    sealed interface FabBottomSheet : HomeEvent {
        data object FabClick : FabBottomSheet
        data object SheetShow : FabBottomSheet
        data object SheetHide : FabBottomSheet
    }

    sealed interface AudioRecorder : HomeEvent {
        data object Idle : AudioRecorder
        data object Record : AudioRecorder
        data object Pause : AudioRecorder
        data object Cancel : AudioRecorder
        data object Done : AudioRecorder
    }

    sealed interface Permission : HomeEvent {
        data class Settings(val type: fyi.manpreet.flowdiary.platform.permission.Permission) : Permission
        data object Close : Permission
    }

    data object Reload : HomeEvent
}
