package fyi.manpreet.flowdiary.ui.settings.state

import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic

sealed interface SettingsEvent {

    data class EmotionUpdate(val emotionType: EmotionType) : SettingsEvent

    sealed interface Topics : SettingsEvent {
        data class SelectedTopicAdd(val topic: Topic) : Topics
        data class SelectedTopicRemove(val topic: Topic) : Topics
        data class SavedTopicsAdd(val topic: Topic) : Topics
        data class IsAddingStatusChange(val status: Boolean) : Topics
        data class SearchQueryChanged(val query: String) : Topics
    }
}