package fyi.manpreet.flowdiary.ui.newrecord.components.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import fyi.manpreet.flowdiary.ui.home.state.Topic
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.settings.components.topic.TopicDropdown
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopicTextField(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    selectedTopics: Set<Topic>,
    onSelectedTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    savedTopics: Set<Topic>,
    onSavedTopicsChange: (NewRecordEvent.Data.Topics) -> Unit,
    isAddingTopic: Boolean,
    onAddingTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (NewRecordEvent.Data.Topics) -> Unit,
    hintText: StringResource,
    imeAction: ImeAction = ImeAction.Next,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TopicsList(
        modifier = modifier,
        icon = icon,
        hintText = hintText,
        imeAction = imeAction,
        selectedTopics = selectedTopics,
        onSelectedTopics = onSelectedTopicChange,
        onAddingTopicChange = onAddingTopicChange,
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        focusRequester = focusRequester,
        keyboardController = keyboardController,
    )

    if (searchQuery.isEmpty()) return

    TopicDropdown(
        modifier = modifier,
        selectedTopics = selectedTopics,
        onSelectedTopicAdd = { onAddingTopicChange(NewRecordEvent.Data.Topics.SelectedTopicsChange(selectedTopics + it)) },
        onAddingTopicChange = { onAddingTopicChange(NewRecordEvent.Data.Topics.IsAddingStatusChange(it)) },
        searchQuery = searchQuery,
        onSearchQueryChange = { onSearchQueryChange(NewRecordEvent.Data.Topics.SearchQueryChanged(it)) },
        savedTopics = savedTopics,
        onSavedTopicsAdd = { onSavedTopicsChange(NewRecordEvent.Data.Topics.SavedTopicsChange(savedTopics + it)) },
    )
}

@Composable
private fun TopicsList(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    hintText: StringResource,
    imeAction: ImeAction,
    selectedTopics: Set<Topic>,
    onSelectedTopics: (NewRecordEvent.Data.Topics) -> Unit,
    onAddingTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (NewRecordEvent.Data.Topics) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = FilterChipDefaults.Height),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {

        Image(
            painter = painterResource(icon),
            modifier = Modifier.size(MaterialTheme.spacing.large),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant),
        )

        ChipFlowRow(
            hintText = hintText,
            imeAction = imeAction,
            selectedTopics = selectedTopics,
            onSelectedTopics = onSelectedTopics,
            onAddingTopicChange = onAddingTopicChange,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            focusRequester = focusRequester,
            keyboardController = keyboardController,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipFlowRow(
    hintText: StringResource,
    imeAction: ImeAction,
    selectedTopics: Set<Topic>,
    onSelectedTopics: (NewRecordEvent.Data.Topics) -> Unit,
    onAddingTopicChange: (NewRecordEvent.Data.Topics) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (NewRecordEvent.Data.Topics) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    // Selected Topics
    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {

        selectedTopics.forEach { topic ->
            fyi.manpreet.flowdiary.ui.home.components.chips.TopicChip(
                modifier = Modifier.padding(
                    end = MaterialTheme.spacing.extraSmall,
                    bottom = MaterialTheme.spacing.extraSmall
                ),
                topic = topic.value,
                shouldShowCancel = true,
                onCancel = {
                    onSelectedTopics(NewRecordEvent.Data.Topics.SelectedTopicsChange(selectedTopics - topic))
                }
            )
        }

        // Search/Add Topic Dialog
        BasicTextField(
            value = searchQuery,
            onValueChange = { onSearchQueryChange(NewRecordEvent.Data.Topics.SearchQueryChanged(it)) },
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = MaterialTheme.spacing.small)
                .then(
                    if (selectedTopics.isNotEmpty()) Modifier.padding(top = MaterialTheme.spacing.small)
                    else Modifier
                )
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = imeAction,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onAddingTopicChange(NewRecordEvent.Data.Topics.IsAddingStatusChange(false))
                }
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (searchQuery.isEmpty() && selectedTopics.isEmpty()) {
                        Text(
                            text = stringResource(hintText),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
