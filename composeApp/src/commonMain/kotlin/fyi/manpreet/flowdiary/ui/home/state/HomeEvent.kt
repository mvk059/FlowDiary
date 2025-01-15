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

    sealed interface AudioDragRecorder : HomeEvent {
        data object Record : AudioDragRecorder
        data class Drag(val offsetX: Float, val isInCancelZone: Boolean) : AudioDragRecorder
        data object Cancel : AudioDragRecorder
        data object Done : AudioDragRecorder
    }

    sealed interface AudioPlayer : HomeEvent {
        data class Play(val id: Long) : AudioPlayer
        data class Pause(val id: Long) : AudioPlayer
    }

    sealed interface Permission : HomeEvent {
        data class Settings(val type: fyi.manpreet.flowdiary.platform.permission.Permission) : Permission
        data object Close : Permission
    }

    data object Reload : HomeEvent
}
