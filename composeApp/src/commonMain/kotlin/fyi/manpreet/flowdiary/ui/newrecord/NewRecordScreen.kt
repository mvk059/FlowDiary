package fyi.manpreet.flowdiary.ui.newrecord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.common_cancel
import flowdiary.composeapp.generated.resources.common_save
import flowdiary.composeapp.generated.resources.ic_edit
import flowdiary.composeapp.generated.resources.ic_hashtag
import flowdiary.composeapp.generated.resources.new_record_add_description
import flowdiary.composeapp.generated.resources.new_record_add_topic
import flowdiary.composeapp.generated.resources.new_record_appbar_title
import flowdiary.composeapp.generated.resources.new_record_title_cd
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.ui.components.appbar.CenterTopAppBar
import fyi.manpreet.flowdiary.ui.components.player.AudioPlayer
import fyi.manpreet.flowdiary.ui.home.state.PlaybackState
import fyi.manpreet.flowdiary.ui.newrecord.components.bottomsheet.EmotionBottomSheet
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonDisabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonPrimaryEnabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.components.dialog.BackConfirmationDialog
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.DescriptionTextField
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.TitleTextField
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.TopicTextField
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordState
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration

@Composable
fun NewRecordScreen(
    viewModel: NewRecordViewModel = koinViewModel(),
    navController: NavController,
    path: AudioPath,
    amplitudePath: String,
) {

    LaunchedEffect(Unit) {
        viewModel.savePath(path, amplitudePath, navController)
    }

    val newRecordState = viewModel.newRecordState.collectAsStateWithLifecycle()
    val playbackState = viewModel.playbackState.collectAsStateWithLifecycle()

    NewRecordScreenContent(
        newRecordState = newRecordState,
        playbackState = playbackState,
        onTitleUpdate = viewModel::onEvent,
        onEmotionTypeSelect = viewModel::onEvent,
        onSelectedTopicChange = viewModel::onEvent,
        onSavedTopicsChange = viewModel::onEvent,
        onAddingTopicChange = viewModel::onEvent,
        onSearchQueryChange = viewModel::onEvent,
        onDescriptionUpdate = viewModel::onEvent,
        onAudioPlayerEvent = viewModel::onEvent,
        onSave = viewModel::onEvent,
        onTopBarBackClick = viewModel::onEvent,
        onBackConfirm = viewModel::onEvent,
        onBottomSheetShow = viewModel::onEvent,
        onBottomSheetDismiss = viewModel::onEvent,
    )

}

@Composable
fun NewRecordScreenContent(
    newRecordState: State<NewRecordState?>,
    playbackState: State<PlaybackState>,
    onTitleUpdate: (NewRecordEvent.Data) -> Unit,
    onEmotionTypeSelect: (NewRecordEvent.Data) -> Unit,
    onSelectedTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    onSavedTopicsChange: (NewRecordEvent.Data.Topics) -> Unit,
    onAddingTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    onSearchQueryChange: (NewRecordEvent.Data.Topics) -> Unit,
    onDescriptionUpdate: (NewRecordEvent.Data) -> Unit,
    onAudioPlayerEvent: (NewRecordEvent.AudioPlayer) -> Unit,
    onSave: (NewRecordEvent.Save) -> Unit,
    onTopBarBackClick: (NewRecordEvent) -> Unit,
    onBackConfirm: (NewRecordEvent) -> Unit,
    onBottomSheetShow: (NewRecordEvent.FabBottomSheet) -> Unit,
    onBottomSheetDismiss: (NewRecordEvent.FabBottomSheet) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                text = stringResource(Res.string.new_record_appbar_title),
                contentDescription = stringResource(Res.string.new_record_title_cd),
                containerColor = MaterialTheme.colorScheme.onPrimary,
                onBackClick = { onTopBarBackClick(NewRecordEvent.BackConfirm(true)) },
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.onPrimary),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {

            val descriptionFieldFocusRequester: FocusRequester = remember { FocusRequester() }

            TitleTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                title = newRecordState.value?.title,
                emotionType = newRecordState.value?.emotionType,
                onAddClick = onBottomSheetShow,
                onTitleUpdate = onTitleUpdate,
                isEmotionSelected = newRecordState.value?.isEmotionSaveButtonEnabled
            )

            Row(
                modifier =  Modifier.fillMaxWidth(),
            ) {

                AudioPlayer(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    amplitudeData = newRecordState.value?.amplitudeData ?: emptyList(),
                    isPlaying = playbackState.value.playingId != null,
                    emotionType = newRecordState.value?.emotionType,
                    currentPosition = playbackState.value.position,
                    totalDuration = newRecordState.value?.totalDuration ?: Duration.ZERO,
                    onPlayPauseClick = {
                        if (playbackState.value.playingId != null) onAudioPlayerEvent(NewRecordEvent.AudioPlayer.Pause)
                        else onAudioPlayerEvent(NewRecordEvent.AudioPlayer.Play)
                    },
                    onSeek = {}
                )
            }

            TopicTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large, vertical = MaterialTheme.spacing.small),
                icon = Res.drawable.ic_hashtag,
                selectedTopics = newRecordState.value?.selectedTopics ?: emptySet(),
                onSelectedTopicChange = onSelectedTopicChange,
                savedTopics = newRecordState.value?.savedTopics ?: emptySet(),
                onSavedTopicsChange = onSavedTopicsChange,
                isAddingTopic = newRecordState.value?.isAddingTopic ?: false,
                onAddingTopicChange = onAddingTopicChange,
                searchQuery = newRecordState.value?.searchQuery ?: "",
                onSearchQueryChange = onSearchQueryChange,
                hintText = Res.string.new_record_add_topic,
                descriptionFieldFocusRequester = descriptionFieldFocusRequester,
            )

            DescriptionTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                descriptionText = newRecordState.value?.description ?: "",
                onDescriptionUpdate = onDescriptionUpdate,
                icon = Res.drawable.ic_edit,
                hintText = Res.string.new_record_add_description,
                imeAction = ImeAction.Done,
                descriptionFieldFocusRequester = descriptionFieldFocusRequester,
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {

                Box(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = MaterialTheme.shapes.large
                        )
                        .noRippleClickable { onTopBarBackClick(NewRecordEvent.BackConfirm(true)) }
                        .weight(0.3f)
                ) {
                    Text(
                        text = stringResource(Res.string.common_cancel),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                if (newRecordState.value?.isSaveButtonEnabled == true) {
                    ButtonPrimaryEnabledNoRipple(
                        modifier = Modifier.weight(0.7f),
                        text = Res.string.common_save,
                        onClick = { onSave(NewRecordEvent.Save) },
                        weight = 0.7f,
                    )
                } else {
                    ButtonDisabledNoRipple(
                        modifier = Modifier.weight(0.7f),
                        text = Res.string.common_save,
                        onClick = {},
                        weight = 0.7f,
                    )
                }
            }
        }

        if (newRecordState.value?.onBackConfirm == true) {
            BackConfirmationDialog(
                onBack = onBackConfirm,
                onCancel = onTopBarBackClick,
            )
        }

        EmotionBottomSheet(
            fabBottomSheet = newRecordState.value?.fabState,
            emotionType = newRecordState.value?.emotionType,
            emotions = newRecordState.value?.emotions,
            emotionsSaveButtonEnabled = newRecordState.value?.isEmotionSaveButtonEnabled ?: false,
            onEmotionTypeSelect = onEmotionTypeSelect,
            onDismiss = onBottomSheetDismiss
        )
    }
}
