package fyi.manpreet.flowdiary.ui.settings.state

import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.home.state.Topic

data class SettingsState(
    val emotions: List<Emotions> = emptyList(),
    val emotionType: EmotionType? = null,
    val selectedTopics: Set<Topic> = emptySet(),
    val savedTopics: Set<Topic> = emptySet(),
    val isAddingTopic: Boolean = false,
    val searchQuery: String = "",
)
