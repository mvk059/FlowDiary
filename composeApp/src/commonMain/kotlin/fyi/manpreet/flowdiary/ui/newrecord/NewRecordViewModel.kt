package fyi.manpreet.flowdiary.ui.newrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import arrow.core.raise.either
import co.touchlab.kermit.Logger
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
import fyi.manpreet.flowdiary.data.model.AmplitudeData
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.di.createGroqClient
import fyi.manpreet.flowdiary.platform.audioplayer.AudioPlayer
import fyi.manpreet.flowdiary.platform.environment.Environment
import fyi.manpreet.flowdiary.platform.filemanager.FileManager
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.home.state.PlaybackState
import fyi.manpreet.flowdiary.ui.home.state.Topic
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordState
import io.github.vyfor.groqkt.GroqClient
import io.github.vyfor.groqkt.GroqModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

class NewRecordViewModel(
    private val repository: AudioRepository,
    private val audioPlayer: AudioPlayer,
    private val fileManager: FileManager,
    private val environment: Environment,
) : ViewModel() {

    private val _recordingPath = MutableStateFlow<AudioPath?>(null)
    private val _amplitudePath = MutableStateFlow<String?>(null)

    private val _newRecordState = MutableStateFlow<NewRecordState?>(NewRecordState())
    val newRecordState: StateFlow<NewRecordState?> = _newRecordState
        .onStart { initRecordState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NewRecordState()
        )

    private val _playbackState = MutableStateFlow(PlaybackState.NotPlaying)
    val playbackState = _playbackState.asStateFlow()

    private var playbackJob: Job? = null

    private lateinit var navController: NavController

    private val groqClient: GroqClient by lazy { createGroqClient(environment) }

    init {
        audioPlayer.setOnPlaybackCompleteListener {
            onStop()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
        playbackJob?.cancel()
    }

    fun savePath(path: AudioPath, amplitudePath: String, navController: NavController) {
        _recordingPath.update { path }
        _amplitudePath.update { amplitudePath }
        this.navController = navController
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
            NewRecordEvent.Transcribe -> onTranscribe()
            NewRecordEvent.Save -> onSave()
            is NewRecordEvent.BackConfirm -> onBackConfirm(event.value)
            NewRecordEvent.NavigateBack -> onNavigateBack()
            NewRecordEvent.FabBottomSheet.SheetShow -> onShowBottomSheet()
            NewRecordEvent.FabBottomSheet.SheetHide -> onHideBottomSheet()
            NewRecordEvent.AudioPlayer.Play -> onPlay()
            NewRecordEvent.AudioPlayer.Pause -> onPause()
        }
    }

    private fun initRecordState() {
        viewModelScope.launch {
            val defaultEmotion = repository.getDefaultEmotion()
            val selectedTopics = repository.getAllSelectedTopics()
            val savedTopics = repository.getAllSavedTopics()
            val emotions = listOf(
                Emotions(
                    type = EmotionType.Excited,
                    selectedIcon = Res.drawable.ic_excited,
                    unselectedIcon = Res.drawable.ic_excited_outline,
                    contentDescription = Res.string.mood_excited,
                    isSelected = EmotionType.Excited == defaultEmotion,
                ),
                Emotions(
                    type = EmotionType.Peaceful,
                    selectedIcon = Res.drawable.ic_peaceful,
                    unselectedIcon = Res.drawable.ic_peaceful_outline,
                    contentDescription = Res.string.mood_peaceful,
                    isSelected = EmotionType.Peaceful == defaultEmotion,
                ),
                Emotions(
                    type = EmotionType.Neutral,
                    selectedIcon = Res.drawable.ic_neutral,
                    unselectedIcon = Res.drawable.ic_neutal_outline,
                    contentDescription = Res.string.mood_neutral,
                    isSelected = EmotionType.Neutral == defaultEmotion,
                ),
                Emotions(
                    type = EmotionType.Sad,
                    selectedIcon = Res.drawable.ic_sad,
                    unselectedIcon = Res.drawable.ic_sad_outline,
                    contentDescription = Res.string.mood_sad,
                    isSelected = EmotionType.Sad == defaultEmotion,
                ),
                Emotions(
                    type = EmotionType.Stressed,
                    selectedIcon = Res.drawable.ic_stressed,
                    unselectedIcon = Res.drawable.ic_stressed_outline,
                    contentDescription = Res.string.mood_stressed,
                    isSelected = EmotionType.Stressed == defaultEmotion,
                ),
            )
            _newRecordState.update {
                NewRecordState(
                    emotions = emotions,
                    emotionType = defaultEmotion,
                    selectedTopics = selectedTopics,
                    savedTopics = savedTopics,
                    totalDuration = audioPlayer.getAudioDuration(_recordingPath.value?.value ?: ""),
                    amplitudeData = getAmplitudeData(_amplitudePath.value ?: ""),
                    fabState = NewRecordEvent.FabBottomSheet.SheetShow,
                    isEmotionSaveButtonEnabled = defaultEmotion != null,
                )
            }
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
        _newRecordState.update { state ->
            state?.copy(
                emotionType = emotionType,
                fabState = NewRecordEvent.FabBottomSheet.SheetHide
            )
        }
        updateSaveButtonState()
    }

    private fun onShowBottomSheet() {
        _newRecordState.update { it?.copy(fabState = NewRecordEvent.FabBottomSheet.SheetShow) }
    }

    private fun onHideBottomSheet() {
        _newRecordState.update { state ->
            state?.copy(
                emotions = state.emotions.map { emotion ->
                    if (emotion.type == state.emotionType) emotion.copy(isSelected = true)
                    else emotion.copy(isSelected = false)
                },
                fabState = NewRecordEvent.FabBottomSheet.SheetHide,
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

    private fun onPlay() {
        playbackJob?.cancel()
        val audioPath = _recordingPath.value
        requireNotNull(audioPath) { "Audio path can not be null." }
        audioPlayer.play(audioPath.value)
        _playbackState.update {
            PlaybackState(
                playingId = PlaybackState.DEFAULT_ID,
                position = Duration.ZERO
            )
        }
        startPositionUpdates(PlaybackState.DEFAULT_ID)
    }

    private fun onPause() {
        audioPlayer.stop()
        playbackJob?.cancel()
        _playbackState.update { PlaybackState.NotPlaying }
    }

    private fun onStop() {
        playbackJob?.cancel()
        _playbackState.update { PlaybackState.NotPlaying }
    }

    private fun startPositionUpdates(id: Long) {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isActive) {
                if (_playbackState.value.playingId == id && !_playbackState.value.isSeeking) {
                    val currentPosition = audioPlayer.getCurrentPosition()
                    Logger.i { "NVM Position : $currentPosition, $id" }
                    _playbackState.update { it.copy(position = currentPosition) }
                }
                delay(100) // Update every 100ms
            }
        }
    }

    private fun onTranscribe() {
        viewModelScope.launch {
            val result = groqClient.transcribeAudio {
                model = GroqModel.DISTIL_WHISPER_LARGE_V3_EN
                file(_recordingPath.value?.value ?: "")
            }
            val data = result.getOrNull()?.data
            _newRecordState.update {
                it?.copy(
                    description = data?.text ?: "",
                    isAITranscribeUsed = true
                )
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val data = _newRecordState.value
            requireNotNull(data) { "Data is null" }

            val title = data.title
            val emotionType = data.emotionType
            val path = _recordingPath.value
            val amplitudePath = _amplitudePath.value
            requireNotNull(title) { "Title is null" }
            requireNotNull(emotionType) { "EmotionType is null" }
            requireNotNull(path) { "Path is null" }
            requireNotNull(amplitudePath) { "amplitudePath is null" }
            val amplitudeData = getAmplitudeData(amplitudePath)

            _newRecordState.value?.selectedTopics?.forEach {
                repository.insertSavedTopic(it)
            }

            val audio = Audio(
                id = Audio.INVALID_ID,
                path = path,
                amplitudePath = amplitudePath,
                createdDateInMillis = 0L,
                title = title,
                emotionType = emotionType,
                topics = data.selectedTopics.toList(),
                description = data.description,
                duration = audioPlayer.getAudioDuration(path.value),
                amplitudeData = amplitudeData
            )
            repository.insertRecording(audio)
            onNavigateBack()
        }
    }

    private fun onBackConfirm(value: Boolean) {
        _newRecordState.update { state -> state?.copy(onBackConfirm = value) }
    }

    private fun onNavigateBack() {
        navController.popBackStack()
    }

    private suspend fun getAmplitudeData(path: String): List<AmplitudeData> {
        return either {
            with(fileManager) {
                this@either.getAmplitudeData(path)
            }
        }.fold(
            ifLeft = { println("Failed to get recording path: $it"); emptyList() },
            ifRight = { return it }
        )
    }
}