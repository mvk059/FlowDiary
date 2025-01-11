package fyi.manpreet.flowdiary.ui.settings.components.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.hashtag_cd
import flowdiary.composeapp.generated.resources.ic_hashtag
import flowdiary.composeapp.generated.resources.new_record_add_icon_cd
import flowdiary.composeapp.generated.resources.settings_add_icon_cd
import flowdiary.composeapp.generated.resources.settings_create_new_topic
import flowdiary.composeapp.generated.resources.settings_my_topics
import flowdiary.composeapp.generated.resources.settings_my_topics_subtitle
import fyi.manpreet.flowdiary.ui.home.state.Topic
import fyi.manpreet.flowdiary.ui.settings.state.SettingsEvent
import fyi.manpreet.flowdiary.ui.theme.Secondary95
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopicsSelection(
    modifier: Modifier = Modifier,
    selectedTopics: Set<Topic>,
    onSelectedTopicAdd: (SettingsEvent.Topics) -> Unit,
    onSelectedTopicRemove: (SettingsEvent.Topics) -> Unit,
    savedTopics: Set<Topic>,
    onSavedTopicsAdd: (SettingsEvent.Topics) -> Unit,
    isAddingTopic: Boolean,
    onAddingTopicChange: (SettingsEvent.Topics) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (SettingsEvent.Topics) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TopicsList(
        modifier = modifier,
        selectedTopics = selectedTopics,
        onSelectedTopicRemove = { onSelectedTopicRemove(SettingsEvent.Topics.SelectedTopicRemove(it)) },
        isAddingTopic = isAddingTopic,
        onAddingTopicChange = { onAddingTopicChange(SettingsEvent.Topics.IsAddingStatusChange(it)) },
        searchQuery = searchQuery,
        onSearchQueryChange = { onSearchQueryChange(SettingsEvent.Topics.SearchQueryChanged(it)) },
        focusRequester = focusRequester,
        keyboardController = keyboardController,
    )

    if (searchQuery.isEmpty()) return

    TopicDropdown(
        modifier = modifier,
        selectedTopics = selectedTopics,
        onSelectedTopicAdd = { onSelectedTopicAdd(SettingsEvent.Topics.SelectedTopicAdd(it)) },
        onAddingTopicChange = { onAddingTopicChange(SettingsEvent.Topics.IsAddingStatusChange(it)) },
        searchQuery = searchQuery,
        onSearchQueryChange = { onSearchQueryChange(SettingsEvent.Topics.SearchQueryChanged(it)) },
        savedTopics = savedTopics,
        onSavedTopicsAdd = { onSavedTopicsAdd(SettingsEvent.Topics.SavedTopicsAdd(it)) },
    )
}

@Composable
private fun TopicsList(
    modifier: Modifier = Modifier,
    selectedTopics: Set<Topic>,
    onSelectedTopicRemove: (Topic) -> Unit,
    isAddingTopic: Boolean,
    onAddingTopicChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = MaterialTheme.spacing.large,
                shape = MaterialTheme.shapes.medium,
                spotColor = Color(0xFF474F60).copy(alpha = 0.08f) // TODO Remove hardcoded color
            ),
        color = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier = Modifier.padding(all = MaterialTheme.spacing.medium)
        ) {

            TitleText()

            ChipFlowRow(
                selectedTopics = selectedTopics,
                onSelectedTopicRemove = onSelectedTopicRemove,
                isAddingTopic = isAddingTopic,
                onAddingTopicChange = onAddingTopicChange,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                focusRequester = focusRequester,
                keyboardController = keyboardController,
            )
        }
    }
}

@Composable
private fun TitleText() {

    Text(
        text = stringResource(Res.string.settings_my_topics),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
        text = stringResource(Res.string.settings_my_topics_subtitle),
        modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipFlowRow(
    selectedTopics: Set<Topic>,
    onSelectedTopicRemove: (Topic) -> Unit,
    isAddingTopic: Boolean,
    onAddingTopicChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    LaunchedEffect(isAddingTopic) {
        if (isAddingTopic.not()) return@LaunchedEffect
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Selected Topics
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.spacing.medium),
    ) {

        selectedTopics.forEach { topic ->
            fyi.manpreet.flowdiary.ui.home.components.chips.TopicChip(
                modifier = Modifier.padding(
                    end = MaterialTheme.spacing.extraSmall,
                    bottom = MaterialTheme.spacing.extraSmall
                ),
                topic = topic.value,
                shouldShowCancel = true,
                onCancel = { onSelectedTopicRemove(topic) }
            )
        }

        if (isAddingTopic.not()) {
            IconButton(
                onClick = { onAddingTopicChange(true) },
                modifier = Modifier
                    .background(color = Secondary95, shape = CircleShape)
                    .size(MaterialTheme.spacing.largeXL),
                content = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(Res.string.new_record_add_icon_cd),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        } else {
            // Search/Add Topic Dialog
            BasicTextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChange(it) },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = MaterialTheme.spacing.small)
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onAddingTopicChange(false)
                    }
                )
            )
        }
    }
}

@Composable
fun TopicDropdown(
    modifier: Modifier = Modifier,
    selectedTopics: Set<Topic>,
    onSelectedTopicAdd: (Topic) -> Unit,
    onAddingTopicChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    savedTopics: Set<Topic>,
    onSavedTopicsAdd: (Topic) -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.large)
            .offset(y = -(MaterialTheme.spacing.medium))
            .shadow(elevation = MaterialTheme.spacing.large, shape = MaterialTheme.shapes.medium),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .verticalScroll(rememberScrollState())
        ) {

            // Filter saved topics based on search query
            val matchingSavedTopics = savedTopics.filter {
                it.value.startsWith(prefix = searchQuery, ignoreCase = true) && !selectedTopics.contains(it)
            }

            // Show matching saved topics first
            matchingSavedTopics.forEach { topic ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                onSelectedTopicAdd(topic)
                                onSearchQueryChange("")
                                onAddingTopicChange(false)
                            }
                        )
                        .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Icon(
                        painter = painterResource(Res.drawable.ic_hashtag),
                        contentDescription = stringResource(Res.string.hashtag_cd),
                        tint = Color.Unspecified,
                    )

                    Text(
                        text = topic.value,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Show create option if query doesn't exist in saved topics
            if (!savedTopics.any { it.value.equals(searchQuery, ignoreCase = true) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val newTopic = Topic(searchQuery.trim())
                            onSavedTopicsAdd(newTopic)
                            onSelectedTopicAdd(newTopic)
                            onSearchQueryChange("")
                            onAddingTopicChange(false)
                        }
                        .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.small),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        modifier = Modifier.size(MaterialTheme.spacing.smallMedium),
                        contentDescription = stringResource(Res.string.settings_add_icon_cd),
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = stringResource(Res.string.settings_create_new_topic, searchQuery),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
