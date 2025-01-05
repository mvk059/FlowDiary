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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
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
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.home.state.Topic
import fyi.manpreet.flowdiary.ui.newrecord.components.bottomsheet.EmotionBottomSheet
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonDisabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonPrimaryEnabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.components.dialog.BackConfirmationDialog
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.DescriptionTextField
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.TitleTextField
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.TopicTextField
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.Peek
import fyi.manpreet.flowdiary.util.noRippleClickable
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewRecordScreen(
    viewModel: NewRecordViewModel = koinViewModel(),
    navController: NavController,
    path: AudioPath,
    onBackClick: () -> Unit,
) {

    val newRecordState = viewModel.newRecordState.collectAsStateWithLifecycle()
    val fabBottomSheet = viewModel.fabBottomSheet.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.savePath(path)
    }

    LaunchedEffect(newRecordState.value) {
        if (newRecordState.value?.onNavigateBack == true) {
            onBackClick()
        }
    }

    val sheetState = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek)
    )

    LaunchedEffect(fabBottomSheet.value) {
        when (fabBottomSheet.value) {
            NewRecordEvent.FabBottomSheet.SheetShow -> scope.launch { sheetState.animateTo(Peek) }
            NewRecordEvent.FabBottomSheet.SheetHide -> scope.launch { sheetState.animateTo(Hidden) }
        }
    }

    NewRecordScreenContent(
        sheetState = sheetState,
        emotions = newRecordState.value?.emotions,
        emotionType = newRecordState.value?.emotionType,
        onEmotionTypeSelect = viewModel::onEvent,
        emotionsSaveButtonEnabled = newRecordState.value?.isEmotionSaveButtonEnabled ?: false,
        title = newRecordState.value?.title,
        onTitleUpdate = viewModel::onEvent,
        selectedTopics = newRecordState.value?.selectedTopics,
        onSelectedTopicChange = viewModel::onEvent,
        savedTopics = newRecordState.value?.savedTopics,
        onSavedTopicsChange = viewModel::onEvent,
        isAddingTopic = newRecordState.value?.isAddingTopic,
        onAddingTopicChange = viewModel::onEvent,
        searchQuery = newRecordState.value?.searchQuery,
        onSearchQueryChange = viewModel::onEvent,
        description = newRecordState.value?.description,
        onDescriptionUpdate = viewModel::onEvent,
        isSaveButtonEnabled = newRecordState.value?.isSaveButtonEnabled,
        isBackDialogVisible = newRecordState.value?.onBackConfirm,
        onBackClick = viewModel::onEvent,
        onBackConfirm = viewModel::onEvent,
        onBottomSheetShow = viewModel::onEvent,
        onBottomSheetDismiss = viewModel::onEvent,
    )
}

@Composable
fun NewRecordScreenContent(
    sheetState: ModalBottomSheetState,
    emotions: List<Emotions>?,
    emotionType: EmotionType?,
    onEmotionTypeSelect: (NewRecordEvent.Data) -> Unit,
    emotionsSaveButtonEnabled: Boolean,
    title: String?,
    onTitleUpdate: (NewRecordEvent.Data) -> Unit,
    selectedTopics: Set<Topic>?,
    onSelectedTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    savedTopics: Set<Topic>?,
    onSavedTopicsChange: (NewRecordEvent.Data.Topics) -> Unit,
    isAddingTopic: Boolean?,
    onAddingTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    searchQuery: String?,
    onSearchQueryChange: (NewRecordEvent.Data.Topics) -> Unit,
    description: String?,
    onDescriptionUpdate: (NewRecordEvent.Data) -> Unit,
    isSaveButtonEnabled: Boolean?,
    isBackDialogVisible: Boolean?,
    onBackClick: (NewRecordEvent) -> Unit,
    onBackConfirm: (NewRecordEvent) -> Unit,
    onBottomSheetShow: (NewRecordEvent.FabBottomSheet) -> Unit,
    onBottomSheetDismiss: (NewRecordEvent.FabBottomSheet) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                text = stringResource(Res.string.new_record_appbar_title),
                contentDescription = stringResource(Res.string.new_record_title_cd),
                containerColor = MaterialTheme.colorScheme.onPrimary,
                onBackClick = { onBackClick(NewRecordEvent.BackConfirm(true)) },
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

            TitleTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                title = title,
                emotionType = emotionType,
                onAddClick = onBottomSheetShow,
                onTitleUpdate = onTitleUpdate,
            )

            TopicTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                icon = Res.drawable.ic_hashtag,
                selectedTopics = selectedTopics ?: emptySet(),
                onSelectedTopicChange = onSelectedTopicChange,
                savedTopics = savedTopics ?: emptySet(),
                onSavedTopicsChange = onSavedTopicsChange,
                isAddingTopic = isAddingTopic ?: false,
                onAddingTopicChange = onAddingTopicChange,
                searchQuery = searchQuery ?: "",
                onSearchQueryChange = onSearchQueryChange,
                hintText = Res.string.new_record_add_topic,
            )

            DescriptionTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                descriptionText = description ?: "",
                onDescriptionUpdate = onDescriptionUpdate,
                icon = Res.drawable.ic_edit,
                hintText = Res.string.new_record_add_description,
                imeAction = ImeAction.Done,
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
                        .noRippleClickable { onBackClick(NewRecordEvent.BackConfirm(true)) }
                        .weight(0.3f)
                ) {
                    Text(
                        text = stringResource(Res.string.common_cancel),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                if (isSaveButtonEnabled == true) {
                    ButtonPrimaryEnabledNoRipple(
                        modifier = Modifier.weight(0.7f),
                        text = Res.string.common_save,
                        onClick = {},
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

        if (isBackDialogVisible == true) {
            BackConfirmationDialog(
                onBack = onBackConfirm,
                onCancel = onBackClick,
            )
        }
    }

    EmotionBottomSheet(
        sheetState = sheetState,
        emotions = emotions,
        emotionsSaveButtonEnabled = emotionsSaveButtonEnabled,
        onEmotionTypeSelect = onEmotionTypeSelect,
        onDismiss = onBottomSheetDismiss
    )
}
