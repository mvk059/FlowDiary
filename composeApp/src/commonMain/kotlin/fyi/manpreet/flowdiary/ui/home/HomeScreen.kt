package fyi.manpreet.flowdiary.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import fyi.manpreet.flowdiary.ui.home.components.appbar.HomeTopAppBar
import fyi.manpreet.flowdiary.ui.home.components.bottomsheet.RecordBottomSheet
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterScreen
import fyi.manpreet.flowdiary.ui.home.components.dialog.PermissionDeniedDialog
import fyi.manpreet.flowdiary.ui.home.components.empty.HomeScreenEmpty
import fyi.manpreet.flowdiary.ui.home.components.fab.HomeFab
import fyi.manpreet.flowdiary.ui.home.components.list.AudioEntryContentItem
import fyi.manpreet.flowdiary.ui.home.components.list.TimelineItem
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import fyi.manpreet.flowdiary.ui.home.state.HomeState
import fyi.manpreet.flowdiary.ui.home.state.PlaybackState
import fyi.manpreet.flowdiary.ui.home.state.Recordings
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNewRecordClick: (AudioPath) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val homeState = viewModel.homeState.collectAsStateWithLifecycle()
    val playbackState = viewModel.playbackState.collectAsStateWithLifecycle()
    val permissionStatus = viewModel.permissionStatus.collectAsStateWithLifecycle()
    val recordingState = viewModel.recordingState.collectAsStateWithLifecycle()

    if (recordingState.value == HomeEvent.AudioRecorder.Done) {
        viewModel.onEvent(HomeEvent.AudioRecorder.Idle)
        val path = homeState.value.recordingPath
        requireNotNull(path) { "Recording path is null." }
        onNewRecordClick(path)
    }

    HomeScreenContent(
        homeState = homeState,
        permissionStatus = permissionStatus,
        recordingState = recordingState,
        playbackState = playbackState,
        onMoodChipItemSelect = viewModel::onEvent,
        onTopicChipItemSelect = viewModel::onEvent,
        onMoodChipReset = viewModel::onEvent,
        onTopicChipReset = viewModel::onEvent,
        onAudioEvent = viewModel::onEvent,
        onAudioPlayerEvent = viewModel::onEvent,
        onBottomSheetShow = viewModel::onEvent,
        onBottomSheetDismiss = viewModel::onEvent,
        onSettingsClick = onSettingsClick,
        onPermissionSettingsSelect = viewModel::onEvent,
        onPermissionDismissSelect = viewModel::onEvent,
    )
}

@Composable
fun HomeScreenContent(
    homeState: State<HomeState>,
    permissionStatus: State<PermissionState>,
    recordingState: State<HomeEvent.AudioRecorder>,
    playbackState: State<PlaybackState>,
    onMoodChipItemSelect: (HomeEvent.Chip) -> Unit,
    onTopicChipItemSelect: (HomeEvent.Chip) -> Unit,
    onMoodChipReset: (HomeEvent.Chip) -> Unit,
    onTopicChipReset: (HomeEvent.Chip) -> Unit,
    onAudioEvent: (HomeEvent.AudioRecorder) -> Unit,
    onAudioPlayerEvent: (HomeEvent.AudioPlayer) -> Unit,
    onBottomSheetShow: (HomeEvent.FabBottomSheet) -> Unit,
    onBottomSheetDismiss: (HomeEvent.FabBottomSheet) -> Unit,
    onSettingsClick: () -> Unit,
    onPermissionSettingsSelect: (HomeEvent.Permission) -> Unit,
    onPermissionDismissSelect: (HomeEvent.Permission) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopAppBar(onSettingsClick = onSettingsClick) },
        floatingActionButton = { HomeFab(onFabClick = onBottomSheetShow) },
    ) { innerPadding ->
        if (homeState.value.recordings.isEmpty()) {
            HomeScreenEmpty(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(brush = MaterialTheme.gradient.background),
            )
        } else {
            RecordingsList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(brush = MaterialTheme.gradient.background),
                recordings = homeState.value.recordings,
                playbackState = playbackState,
                moodChip = homeState.value.moodChip,
                topicsChip = homeState.value.topicsChip,
                onMoodChipItemSelect = onMoodChipItemSelect,
                onTopicChipItemSelect = onTopicChipItemSelect,
                onMoodChipReset = onMoodChipReset,
                onTopicChipReset = onTopicChipReset,
                onAudioPlayerEvent = onAudioPlayerEvent
            )
        }

        if (permissionStatus.value == PermissionState.DENIED) {
            PermissionDeniedDialog(
                onSettingsClick = onPermissionSettingsSelect,
                onDismissRequest = onPermissionDismissSelect,
            )
        }

        RecordBottomSheet(
            fabState = homeState.value.fabBottomSheet,
            onDismiss = onBottomSheetDismiss,
            audioEvent = recordingState.value,
            onAudioEvent = onAudioEvent,
        )
    }
}

@Composable
private fun RecordingsList(
    modifier: Modifier = Modifier,
    recordings: List<Recordings>,
    playbackState: State<PlaybackState>,
    moodChip: FilterOption?,
    topicsChip: FilterOption?,
    onMoodChipItemSelect: (HomeEvent.Chip) -> Unit,
    onTopicChipItemSelect: (HomeEvent.Chip) -> Unit,
    onMoodChipReset: (HomeEvent.Chip) -> Unit,
    onTopicChipReset: (HomeEvent.Chip) -> Unit,
    onAudioPlayerEvent: (HomeEvent.AudioPlayer) -> Unit,
) {
    Column(modifier = modifier) {
        FilterScreen(
            moodChip = moodChip,
            topicsChip = topicsChip,
            onMoodChipItemSelect = onMoodChipItemSelect,
            onTopicChipItemSelect = onTopicChipItemSelect,
            onMoodChipReset = onMoodChipReset,
            onTopicChipReset = onTopicChipReset,
        )

        LazyColumn {
            recordings.forEach { recording ->
                when (recording) {
                    is Recordings.Date -> {
                        item { DateHeader(date = recording.date) }
                    }

                    is Recordings.Entry -> {
                        itemsIndexed(
                            items = recording.recordings,
                            key = { _, it -> it.id }
                        ) { index, audio ->
                            val isPlaying = remember(playbackState.value.playingId) {
                                playbackState.value.playingId == audio.id
                            }
                            val currentPosition = remember(
                                playbackState.value.playingId,
                                playbackState.value.position
                            ) {
                                if (playbackState.value.playingId == audio.id) playbackState.value.position
                                else Duration.ZERO
                            }
                            AudioItem(
                                audio = audio,
                                isPlaying = isPlaying,
                                currentPosition = currentPosition,
                                isLastIndex = index == recording.recordings.lastIndex,
                                onAudioPlayerEvent = onAudioPlayerEvent,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(
    date: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = date,
        modifier = modifier
            .padding(start = MaterialTheme.spacing.small)
            .padding(
                top = MaterialTheme.spacing.large,
                bottom = MaterialTheme.spacing.medium
            ),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun AudioItem(
    modifier: Modifier = Modifier,
    audio: Audio,
    isPlaying: Boolean,
    isLastIndex: Boolean,
    currentPosition: Duration,
    onAudioPlayerEvent: (HomeEvent.AudioPlayer) -> Unit,
) {
    TimelineItem(
        emotionType = audio.emotionType,
        isLastItem = isLastIndex,
        content = { timelineModifier ->
            AudioEntryContentItem(
                modifier = timelineModifier.then(modifier),
                recording = audio,
                currentPosition = currentPosition,
                isPlaying = isPlaying,
                onAudioPlayerEvent = onAudioPlayerEvent
            )
        }
    )
}
