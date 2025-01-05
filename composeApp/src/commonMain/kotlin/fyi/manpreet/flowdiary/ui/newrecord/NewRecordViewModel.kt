package fyi.manpreet.flowdiary.ui.newrecord

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
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.home.state.Topic
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewRecordViewModel(
    private val repository: AudioRepository,
) : ViewModel() {

    private val _recordingPath = MutableStateFlow<AudioPath?>(null)
    val recordingPath: StateFlow<AudioPath?> = _recordingPath.asStateFlow()

    private val _recordingState = MutableStateFlow<Audio?>(null)
    val recordingState: StateFlow<Audio?> = _recordingState.asStateFlow()

    private val _newRecordState = MutableStateFlow<NewRecordState?>(NewRecordState())
    val newRecordState: StateFlow<NewRecordState?> = _newRecordState
        .onStart { initRecordState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NewRecordState()
        )

    private val _fabBottomSheet =
        MutableStateFlow<NewRecordEvent.FabBottomSheet>(NewRecordEvent.FabBottomSheet.SheetHide)
    val fabBottomSheet: StateFlow<NewRecordEvent.FabBottomSheet> = _fabBottomSheet.asStateFlow()

    fun savePath(path: AudioPath) {
        _recordingPath.update { path }
    }

    fun onEvent(event: NewRecordEvent) {
        when (event) {
            is NewRecordEvent.Data.UpdateTitle -> onTitleUpdated(event.title)
            is NewRecordEvent.Data.UpdateEmotion -> onEmotionUpdated(event.type)
            is NewRecordEvent.Data.SaveEmotion -> onEmotionSave(event.type)
            is NewRecordEvent.Data.Topics.SelectedTopicsChange -> onSelectedTopicsChange(event.topics)
            is NewRecordEvent.Data.Topics.SavedTopicsChange -> onSavedTopicsChange(event.savedTopics)
            is NewRecordEvent.Data.Topics.IsAddingStatusChange -> onIsAddingStatusChange(event.status)
            is NewRecordEvent.Data.Topics.SearchQueryChanged -> onSearchQueryChanged(event.query)
            is NewRecordEvent.Data.UpdateDescription -> onDescriptionUpdated(event.description)
            NewRecordEvent.Save -> onSave()
            is NewRecordEvent.BackConfirm -> onBackConfirm(event.value)
            NewRecordEvent.NavigateBack -> onNavigateBack()
            NewRecordEvent.FabBottomSheet.SheetShow -> onShowBottomSheet()
            NewRecordEvent.FabBottomSheet.SheetHide -> onHideBottomSheet()
        }
    }

    private fun initRecordState() {
        val emotions = listOf(
            Emotions(
                type = EmotionType.Excited,
                selectedIcon = Res.drawable.ic_excited,
                unselectedIcon = Res.drawable.ic_excited_outline,
                contentDescription = Res.string.mood_excited,
            ),
            Emotions(
                type = EmotionType.Peaceful,
                selectedIcon = Res.drawable.ic_peaceful,
                unselectedIcon = Res.drawable.ic_peaceful_outline,
                contentDescription = Res.string.mood_peaceful,
            ),
            Emotions(
                type = EmotionType.Neutral,
                selectedIcon = Res.drawable.ic_neutral,
                unselectedIcon = Res.drawable.ic_neutal_outline,
                contentDescription = Res.string.mood_neutral,
            ),
            Emotions(
                type = EmotionType.Sad,
                selectedIcon = Res.drawable.ic_sad,
                unselectedIcon = Res.drawable.ic_sad_outline,
                contentDescription = Res.string.mood_sad,
            ),
            Emotions(
                type = EmotionType.Stressed,
                selectedIcon = Res.drawable.ic_stressed,
                unselectedIcon = Res.drawable.ic_stressed_outline,
                contentDescription = Res.string.mood_stressed,
            ),
        )
        _newRecordState.update {
            NewRecordState(
                emotions = emotions,
            )
        }
    }

    private fun onTitleUpdated(title: String) {
        _newRecordState.update { state -> state?.copy(title = title) }
        updateSaveButtonState()
    }

    private fun onEmotionUpdated(emotionType: EmotionType) {
        _newRecordState.update { state ->
            state?.copy(
                emotions = state.emotions.map { emotion ->
                    if (emotion.type == emotionType) emotion.copy(isSelected = true)
                    else emotion.copy(isSelected = false)
                },
                isEmotionSaveButtonEnabled = true,
            )
        }
        updateSaveButtonState()
    }

    private fun updateSaveButtonState() {
        _newRecordState.update { state ->
            state?.copy(
                isSaveButtonEnabled = _newRecordState.value?.title?.isNotEmpty() == true && _newRecordState.value?.emotionType != null
            )
        }
    }

    private fun onEmotionSave(emotionType: EmotionType) {
        _newRecordState.update { state -> state?.copy(emotionType = emotionType) }
        _fabBottomSheet.update { NewRecordEvent.FabBottomSheet.SheetHide }
    }

    private fun onShowBottomSheet() {
        _fabBottomSheet.update { NewRecordEvent.FabBottomSheet.SheetShow }
    }

    private fun onHideBottomSheet() {
        _fabBottomSheet.update { NewRecordEvent.FabBottomSheet.SheetHide }
        _newRecordState.update { state ->
            state?.copy(
                emotions = state.emotions.map { emotion ->
                    if (emotion.type == state.emotionType) emotion.copy(isSelected = true)
                    else emotion.copy(isSelected = false)
                },
            )
        }
        updateSaveButtonState()
    }

    private fun onSelectedTopicsChange(topics: Set<Topic>) {
        _newRecordState.update { state -> state?.copy(selectedTopics = topics) }
    }

    private fun onSavedTopicsChange(savedTopics: Set<Topic>) {
        _newRecordState.update { state -> state?.copy(savedTopics = savedTopics) }
    }

    private fun onIsAddingStatusChange(status: Boolean) {
        _newRecordState.update { state -> state?.copy(isAddingTopic = status) }
    }

    private fun onSearchQueryChanged(query: String) {
        _newRecordState.update { state -> state?.copy(searchQuery = query) }
    }

    private fun onDescriptionUpdated(description: String) {
        _newRecordState.update { state -> state?.copy(description = description) }
    }

    private fun onSave() {
        viewModelScope.launch {
            val data = _newRecordState.value
            requireNotNull(data) { "Data is null" }
            val title = data.title
            val emotionType = data.emotionType
            requireNotNull(title) { "Title is null" }
            requireNotNull(emotionType) { "EmotionType is null" }
            val audio = Audio(
                id = Audio.INVALID_ID,
                path = _recordingPath.value,
                createdDateInMillis = 0L,
                title = title,
                emotionType = emotionType,
                topics = data.topics,
                description = data.description,
            )
            repository.insertRecording(audio)
            onNavigateBack()
        }
    }

    private fun onBackConfirm(value: Boolean) {
        _newRecordState.update { state -> state?.copy(onBackConfirm = value) }
    }

    private fun onNavigateBack() {
        _newRecordState.update { state -> state?.copy(onNavigateBack = true) }
    }
}