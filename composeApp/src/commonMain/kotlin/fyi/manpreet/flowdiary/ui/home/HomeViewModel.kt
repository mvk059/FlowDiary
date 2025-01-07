package fyi.manpreet.flowdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_excited
import flowdiary.composeapp.generated.resources.ic_neutral
import flowdiary.composeapp.generated.resources.ic_peaceful
import flowdiary.composeapp.generated.resources.ic_sad
import flowdiary.composeapp.generated.resources.ic_stressed
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.platform.audioplayer.AudioPlayer
import fyi.manpreet.flowdiary.platform.audiorecord.AudioRecorder
import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import fyi.manpreet.flowdiary.platform.permission.service.PermissionService
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import fyi.manpreet.flowdiary.ui.home.state.HomeState
import fyi.manpreet.flowdiary.ui.home.state.Recordings
import fyi.manpreet.flowdiary.util.toRecordingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val audioPlayer: AudioPlayer, // TODO Use case
    private val audioRecorder: AudioRecorder,
    private val permissionService: PermissionService,
    private val repository: AudioRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState
        .onStart { initHomeState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeState()
        )

    private val _permissionStatus = MutableStateFlow(PermissionState.NOT_DETERMINED)
    val permissionStatus: StateFlow<PermissionState> = _permissionStatus
        .onStart {}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = PermissionState.NOT_DETERMINED
        )

    private val _recordingState = MutableStateFlow<HomeEvent.AudioRecorder>(HomeEvent.AudioRecorder.Idle)
    val recordingState: StateFlow<HomeEvent.AudioRecorder> = _recordingState.asStateFlow()

    init {
        audioPlayer.setOnPlaybackCompleteListener {
            val updatedRecordings = _homeState.value.recordings.map { recording ->
                when (recording) {
                    is Recordings.Date -> recording
                    is Recordings.Entry -> {
                        val updatedAudioList = recording.recordings.map { audio -> audio.copy(isPlaying = false) }
                        recording.copy(recordings = updatedAudioList)
                    }
                }
            }
            _homeState.update { it.copy(recordings = updatedRecordings) }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Chip.MoodChip -> onMoodChipSelect(event.id)
            is HomeEvent.Chip.TopicChip -> onTopicChipSelect(event.id)
            HomeEvent.Chip.MoodReset -> onMoodChipReset()
            HomeEvent.Chip.TopicReset -> onTopicChipReset()
            HomeEvent.FabBottomSheet.FabClick -> viewModelScope.launch { checkPermission() }
            HomeEvent.FabBottomSheet.SheetShow -> onFabBottomSheetShow()
            HomeEvent.FabBottomSheet.SheetHide -> onFabBottomSheetHide()
            HomeEvent.Permission.Close -> _permissionStatus.update { PermissionState.NOT_DETERMINED }
            is HomeEvent.Permission.Settings -> openSettingsPage(event.type)
            HomeEvent.AudioRecorder.Idle -> onAudioRecordIdle()
            HomeEvent.AudioRecorder.Record -> onAudioRecordStart()
            HomeEvent.AudioRecorder.Pause -> onAudioRecordPause()
            HomeEvent.AudioRecorder.Cancel -> onAudioRecordCancel()
            HomeEvent.AudioRecorder.Done -> onAudioRecordDone()
            is HomeEvent.AudioPlayer.Play -> onPlay(event.id)
            is HomeEvent.AudioPlayer.Pause -> onPause(event.id)
            HomeEvent.Reload -> onReload()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    private fun initHomeState() {
        val moodChip = FilterOption(
            title = "All Moods",
            options = listOf(
                FilterOption.Options(id = 1, text = "Excited", icon = Res.drawable.ic_excited),
                FilterOption.Options(id = 2, text = "Peaceful", icon = Res.drawable.ic_peaceful),
                FilterOption.Options(id = 3, text = "Neutral", icon = Res.drawable.ic_neutral),
                FilterOption.Options(id = 4, text = "Sad", icon = Res.drawable.ic_sad),
                FilterOption.Options(id = 5, text = "Stressed", icon = Res.drawable.ic_stressed),
            )
        )
        val topicChip = FilterOption(
            title = "All Topics",
            options = listOf(),
        )
        viewModelScope.launch {
            val allRecordings: List<Audio> = repository.getAllRecordings()
            _homeState.update { state ->
                state.copy(
                    recordings = allRecordings.toRecordingList(),
                    moodChip = moodChip,
                    topicsChip = topicChip
                )
            }
        }
    }

    private fun onMoodChipSelect(id: Int) {
        val moodChip = _homeState.value.moodChip
        require(moodChip != null) { "Mood chip can not be null." }

        val options = moodChip.options.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }

        _homeState.update { state -> state.copy(moodChip = state.moodChip?.copy(options = options)) }
    }

    private fun onTopicChipSelect(id: Int) {
        val topicsChip = _homeState.value.topicsChip
        require(topicsChip != null) { "Topic chip can not be null." }

        val options = topicsChip.options.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }

        _homeState.update { state -> state.copy(moodChip = state.topicsChip?.copy(options = options)) }
    }

    private fun onMoodChipReset() {
        val moodChip = _homeState.value.moodChip
        require(moodChip != null) { "Mood chip can not be null." }

        val options = moodChip.options.map { it.copy(isSelected = false) }
        _homeState.update { state -> state.copy(moodChip = state.moodChip?.copy(options = options)) }
    }

    private fun onTopicChipReset() {
        val topicsChip = _homeState.value.topicsChip
        require(topicsChip != null) { "Topic chip can not be null." }

        val options = topicsChip.options.map { it.copy(isSelected = false) }
        _homeState.update { state -> state.copy(moodChip = state.topicsChip?.copy(options = options)) }
    }

    private fun onFabBottomSheetShow() {
        _homeState.update { state -> state.copy(fabBottomSheet = HomeEvent.FabBottomSheet.SheetShow) }
    }

    private fun onFabBottomSheetHide() {
        _homeState.update { state -> state.copy(fabBottomSheet = HomeEvent.FabBottomSheet.SheetHide) }
        viewModelScope.launch {
            if (audioRecorder.isRecording()) audioRecorder.discardRecording()
        }
    }

    private fun onAudioRecordIdle() {
        _recordingState.update { HomeEvent.AudioRecorder.Idle }
        onFabBottomSheetHide()
    }

    private fun onAudioRecordStart() {
        viewModelScope.launch {
            _recordingState.update { HomeEvent.AudioRecorder.Record }
            if (audioRecorder.isPaused()) {
                audioRecorder.resumeRecording()
            } else {
                val fileName = "recording_${Clock.System.now().toEpochMilliseconds()}.m4a"
                audioRecorder.startRecording(fileName)
            }
        }
    }

    private fun onAudioRecordPause() {
        if (audioRecorder.isRecording().not()) return
        viewModelScope.launch {
            _recordingState.update { HomeEvent.AudioRecorder.Pause }
            audioRecorder.pauseRecording()
        }
    }

    private fun onAudioRecordCancel() {
        viewModelScope.launch {
            _recordingState.update { HomeEvent.AudioRecorder.Cancel }
            audioRecorder.discardRecording()
        }
    }

    private fun onAudioRecordDone() {
        viewModelScope.launch {
            _recordingState.update { HomeEvent.AudioRecorder.Done }
            val filePath = audioRecorder.stopRecording()
            Logger.i { "Audio recording done: $filePath" }
            _homeState.update { it.copy(recordingPath = AudioPath(filePath)) }
        }
    }

    private fun onPlay(id: Long) {
        var audioPath: String? = ""
        val updatedRecordings = _homeState.value.recordings.map { recording ->
            when (recording) {
                is Recordings.Date -> recording
                is Recordings.Entry -> {
                    val updatedAudioList = recording.recordings.map { audio ->
                        if (audio.id == id) {
                            audioPath = audio.path?.value //?.substringBeforeLast("/") + "/sound.wav"
                            Logger.i { "Audio path: $audioPath" }
                            audio.copy(isPlaying = true)
                        } else {
                            audio.copy(isPlaying = false)
                        }
                    }
                    recording.copy(recordings = updatedAudioList)
                }
            }
        }
        requireNotNull(audioPath) { "Audio path can not be null." }
        audioPlayer.play(audioPath!!)
        _homeState.update { it.copy(recordings = updatedRecordings) }
    }

    private fun onPause(id: Long) {
        audioPlayer.stop()
        val updatedRecordings = _homeState.value.recordings.map { recording ->
            when (recording) {
                is Recordings.Date -> recording
                is Recordings.Entry -> {
                    val updatedAudioList = recording.recordings.map { audio ->
                        if (audio.id == id) audio.copy(isPlaying = false)
                         else audio
                    }
                    recording.copy(recordings = updatedAudioList)
                }
            }
        }
        _homeState.update { it.copy(recordings = updatedRecordings) }
    }

    private suspend fun checkPermission() {
        val permissionState = permissionService.checkPermission(Permission.MICROPHONE)
        Logger.i { "Permission state: $permissionState" }

        when (permissionState) {
            PermissionState.NOT_DETERMINED -> {
                observePermissionChanges()
                permissionService.providePermission(Permission.MICROPHONE)
            }

            PermissionState.DENIED -> _permissionStatus.update { PermissionState.DENIED }
            PermissionState.GRANTED -> {
                _permissionStatus.update { PermissionState.GRANTED }
                showSheetAndStartRecording()
            }
        }
    }

    private fun observePermissionChanges() {
        viewModelScope.launch {
            permissionService.checkPermissionFlow(Permission.MICROPHONE)
                .collect { state ->
                    Logger.i { "Permission state in flow: $state" }
                    _permissionStatus.update { state }
                    if (state == PermissionState.GRANTED) {
                        showSheetAndStartRecording()
                    }
                }
        }
    }

    private fun openSettingsPage(permission: Permission) {
        permissionService.openSettingsPage(permission)
    }

    private fun onReload() {
        onFabBottomSheetHide()
    }

    private fun showSheetAndStartRecording() {
        onFabBottomSheetShow()
        onAudioRecordStart()
    }
}
