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
import fyi.manpreet.flowdiary.platform.audio.AudioPlayer
import fyi.manpreet.flowdiary.platform.audiorecord.AudioRecorder
import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import fyi.manpreet.flowdiary.platform.permission.service.PermissionService
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
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
) : ViewModel() {

    private val _moodChip = MutableStateFlow<FilterOption?>(null)
    val moodChip = _moodChip
        .onStart { initMoodChip() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val _topicsChip = MutableStateFlow<FilterOption?>(null)
    val topicsChip = _topicsChip
        .onStart { initTopicChip() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
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

    private val _fabBottomSheet = MutableStateFlow<HomeEvent.FabBottomSheet>(HomeEvent.FabBottomSheet.SheetHide)
    val fabBottomSheet: StateFlow<HomeEvent.FabBottomSheet> = _fabBottomSheet.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Chip.MoodChip -> onMoodChipSelect(event.id)
            is HomeEvent.Chip.TopicChip -> onTopicChipSelect(event.id)
            HomeEvent.Chip.MoodReset -> onMoodChipReset()
            HomeEvent.Chip.TopicReset -> onTopicChipReset()
            HomeEvent.FabBottomSheet.FabClick -> viewModelScope.launch { checkPermission() }
            HomeEvent.FabBottomSheet.SheetShow -> onFabBottomSheetShow()
            HomeEvent.FabBottomSheet.SheetHide -> onFabBottomSheetHide()
            HomeEvent.AudioRecorder.Idle -> onAudioRecordIdle()
            HomeEvent.AudioRecorder.Record -> onAudioRecordStart()
            HomeEvent.AudioRecorder.Pause -> onAudioRecordPause()
            HomeEvent.AudioRecorder.Resume -> onAudioRecordResume()
            HomeEvent.AudioRecorder.Cancel -> onAudioRecordCancel()
            HomeEvent.AudioRecorder.Done -> onAudioRecordDone()
            HomeEvent.Permission.Close -> _permissionStatus.update { PermissionState.NOT_DETERMINED }
            is HomeEvent.Permission.Settings -> openSettingsPage(event.type)

        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    private fun initMoodChip() {
        val filterOption = FilterOption(
            title = "All Moods",
            options = listOf(
                FilterOption.Options(id = 1, text = "Excited", icon = Res.drawable.ic_excited),
                FilterOption.Options(id = 2, text = "Peaceful", icon = Res.drawable.ic_peaceful),
                FilterOption.Options(id = 3, text = "Neutral", icon = Res.drawable.ic_neutral),
                FilterOption.Options(id = 4, text = "Sad", icon = Res.drawable.ic_sad),
                FilterOption.Options(id = 5, text = "Stressed", icon = Res.drawable.ic_stressed),
            )
        )
        _moodChip.update { filterOption }
    }

    private fun initTopicChip() {
        val filterOption = FilterOption(
            title = "All Topics",
            options = listOf(),
        )
        _topicsChip.update { filterOption }
    }

    private fun onMoodChipSelect(id: Int) {
        val moodChip = _moodChip.value
        require(moodChip != null) { "Mood chip can not be null." }

        val options = moodChip.options.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }

        _moodChip.update { it?.copy(options = options) }
    }

    private fun onTopicChipSelect(id: Int) {
        val topicsChip = _topicsChip.value
        require(topicsChip != null) { "Topic chip can not be null." }

        val options = topicsChip.options.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }

        _topicsChip.update { it?.copy(options = options) }
    }

    private fun onMoodChipReset() {
        val moodChip = _moodChip.value
        require(moodChip != null) { "Mood chip can not be null." }

        val options = moodChip.options.map { it.copy(isSelected = false) }
        _moodChip.update { it?.copy(options = options) }
    }

    private fun onTopicChipReset() {
        val topicsChip = _topicsChip.value
        require(topicsChip != null) { "Topic chip can not be null." }

        val options = topicsChip.options.map { it.copy(isSelected = false) }
        _topicsChip.update { it?.copy(options = options) }
    }

    private fun onFabBottomSheetShow() {
        _fabBottomSheet.update { HomeEvent.FabBottomSheet.SheetShow }
    }

    private fun onFabBottomSheetHide() {
        _fabBottomSheet.update { HomeEvent.FabBottomSheet.SheetHide }
    }

    private fun onAudioRecordIdle() {
        _recordingState.update { HomeEvent.AudioRecorder.Idle }
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

    private fun onAudioRecordResume() {
        if (audioRecorder.isPaused().not()) return
        viewModelScope.launch {
            _recordingState.update { HomeEvent.AudioRecorder.Resume }
            audioRecorder.resumeRecording()
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
            _recordingState.update { HomeEvent.AudioRecorder.Idle }
            val filePath = audioRecorder.stopRecording()
            Logger.i { "Audio recording done: $filePath" }
        }
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
                _fabBottomSheet.update { HomeEvent.FabBottomSheet.SheetShow }
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
                        _fabBottomSheet.update { HomeEvent.FabBottomSheet.SheetShow }
                    }
                }
        }
    }

    private fun openSettingsPage(permission: Permission) {
        permissionService.openSettingsPage(permission)
    }
}
