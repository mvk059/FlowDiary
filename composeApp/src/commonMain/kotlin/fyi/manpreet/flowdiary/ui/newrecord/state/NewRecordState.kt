package fyi.manpreet.flowdiary.ui.newrecord.state

import androidx.compose.runtime.Stable
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.home.state.Topic

@Stable
data class NewRecordState(
    val title: String? = null,
    val emotionType: EmotionType? = null,
    val emotions: List<Emotions> = emptyList(),
    val selectedTopics: Set<Topic> = emptySet(),
    val savedTopics: Set<Topic> = setOf(Topic("Work"), Topic("Love"), Topic("Jack"), Topic("Jared")), //emptySet(),
    val isAddingTopic: Boolean = false,
    val searchQuery: String = "",
    val description: String = "",
    val onBackConfirm: Boolean = false,
    val isEmotionSaveButtonEnabled: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
    val fabState: NewRecordEvent.FabBottomSheet = NewRecordEvent.FabBottomSheet.SheetHide,
)
