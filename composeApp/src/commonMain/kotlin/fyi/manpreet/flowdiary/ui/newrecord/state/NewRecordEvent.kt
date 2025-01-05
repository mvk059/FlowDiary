package fyi.manpreet.flowdiary.ui.newrecord.state

import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent

sealed interface NewRecordEvent {

    sealed interface Data : NewRecordEvent {
        data class UpdateTitle(val title: String) : Data
        data class UpdateEmotion(val type: EmotionType) : Data
        data class SaveEmotion(val type: EmotionType) : Data
        data class UpdateTopic(val topic: String) : Data
        data class UpdateDescription(val description: String) : Data
    }

    sealed interface FabBottomSheet : NewRecordEvent {
        data object SheetShow : FabBottomSheet
        data object SheetHide : FabBottomSheet
    }

    data object Save : NewRecordEvent
    data class BackConfirm(val value: Boolean) : NewRecordEvent
    data object NavigateBack : NewRecordEvent
}
