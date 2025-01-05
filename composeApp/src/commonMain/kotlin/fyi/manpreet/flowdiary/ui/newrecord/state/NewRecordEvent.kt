package fyi.manpreet.flowdiary.ui.newrecord.state

import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic

sealed interface NewRecordEvent {

    sealed interface Data : NewRecordEvent {
        data class UpdateTitle(val title: String) : Data
        data class UpdateEmotion(val type: EmotionType) : Data
        data class SaveEmotion(val type: EmotionType) : Data

        sealed interface Topics : NewRecordEvent {
            data class SelectedTopicsChange(val topics: Set<Topic>) : Topics
            data class SavedTopicsChange(val savedTopics: Set<Topic>) : Topics
            data class IsAddingStatusChange(val status: Boolean)   : Topics
            data class SearchQueryChanged(val query: String) : Topics
        }

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
