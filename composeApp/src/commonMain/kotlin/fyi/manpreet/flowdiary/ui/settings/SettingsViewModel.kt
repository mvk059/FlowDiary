package fyi.manpreet.flowdiary.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_excited
import flowdiary.composeapp.generated.resources.ic_excited_outline
import flowdiary.composeapp.generated.resources.ic_neutal_outline
import flowdiary.composeapp.generated.resources.ic_neutral
import flowdiary.composeapp.generated.resources.ic_peaceful
import flowdiary.composeapp.generated.resources.ic_peaceful_outline
import flowdiary.composeapp.generated.resources.ic_sad
import flowdiary.composeapp.generated.resources.ic_sad_outline
import flowdiary.composeapp.generated.resources.ic_stressed
import flowdiary.composeapp.generated.resources.ic_stressed_outline
import flowdiary.composeapp.generated.resources.mood_excited
import flowdiary.composeapp.generated.resources.mood_neutral
import flowdiary.composeapp.generated.resources.mood_peaceful
import flowdiary.composeapp.generated.resources.mood_sad
import flowdiary.composeapp.generated.resources.mood_stressed
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.home.state.Topic
import fyi.manpreet.flowdiary.ui.settings.state.SettingsEvent
import fyi.manpreet.flowdiary.ui.settings.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: AudioRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart { initSettingsState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SettingsState(),
        )

    private fun initSettingsState() {
        viewModelScope.launch {
            val defaultEmotionType = repository.getDefaultEmotion()
            val selectedTopics = repository.getAllSelectedTopics()
            val savedTopics = repository.getAllSavedTopics()
            val emotions = listOf(
                Emotions(
                    type = EmotionType.Excited,
                    selectedIcon = Res.drawable.ic_excited,
                    unselectedIcon = Res.drawable.ic_excited_outline,
                    contentDescription = Res.string.mood_excited,
                    isSelected = EmotionType.Excited == defaultEmotionType,
                ),
                Emotions(
                    type = EmotionType.Peaceful,
                    selectedIcon = Res.drawable.ic_peaceful,
                    unselectedIcon = Res.drawable.ic_peaceful_outline,
                    contentDescription = Res.string.mood_peaceful,
                    isSelected = EmotionType.Peaceful == defaultEmotionType,
                ),
                Emotions(
                    type = EmotionType.Neutral,
                    selectedIcon = Res.drawable.ic_neutral,
                    unselectedIcon = Res.drawable.ic_neutal_outline,
                    contentDescription = Res.string.mood_neutral,
                    isSelected = EmotionType.Neutral == defaultEmotionType,
                ),
                Emotions(
                    type = EmotionType.Sad,
                    selectedIcon = Res.drawable.ic_sad,
                    unselectedIcon = Res.drawable.ic_sad_outline,
                    contentDescription = Res.string.mood_sad,
                    isSelected = EmotionType.Sad == defaultEmotionType,
                ),
                Emotions(
                    type = EmotionType.Stressed,
                    selectedIcon = Res.drawable.ic_stressed,
                    unselectedIcon = Res.drawable.ic_stressed_outline,
                    contentDescription = Res.string.mood_stressed,
                    isSelected = EmotionType.Stressed == defaultEmotionType,
                ),
            )
            _state.update { state ->
                state.copy(
                    emotionType = defaultEmotionType,
                    emotions = emotions,
                    selectedTopics = selectedTopics,
                    savedTopics = savedTopics,
                )
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.EmotionUpdate -> onEmotionUpdate(event.emotionType)
            is SettingsEvent.Topics.SelectedTopicAdd -> onSelectedTopicAdd(event.topic)
            is SettingsEvent.Topics.SelectedTopicRemove -> onSelectedTopicRemove(event.topic)
            is SettingsEvent.Topics.SavedTopicsAdd -> onSavedTopicsAdd(event.topic)
            is SettingsEvent.Topics.IsAddingStatusChange -> onIsAddingStatusChange(event.status)
            is SettingsEvent.Topics.SearchQueryChanged -> onSearchQueryChanged(event.query)
        }
    }

    private fun onEmotionUpdate(emotionType: EmotionType) {
        _state.update { state ->
            val emotions = state.emotions.map {
                if (it.type == emotionType) it.copy(isSelected = true)
                else it.copy(isSelected = false)
            }
            state.copy(emotions = emotions)
        }
        viewModelScope.launch {
            repository.setDefaultEmotion(emotionType = emotionType)
        }
    }

    private fun onSelectedTopicAdd(topic: Topic) {
        _state.update { state -> state.copy(selectedTopics = state.selectedTopics + topic) }
        viewModelScope.launch {
            repository.insertSelectedTopic(topic)
        }
    }

    private fun onSelectedTopicRemove(topic: Topic) {
        _state.update { state -> state.copy(selectedTopics = state.selectedTopics - topic) }
        viewModelScope.launch {
            repository.removeSelectedTopic(topic)
        }
    }

    private fun onSavedTopicsAdd(topic: Topic) {
        _state.update { state -> state.copy(savedTopics = state.savedTopics + topic) }
    }

    private fun onIsAddingStatusChange(status: Boolean) {
        _state.update { state -> state.copy(isAddingTopic = status) }
    }

    private fun onSearchQueryChanged(query: String) {
        _state.update { state -> state.copy(searchQuery = query) }
    }
}
