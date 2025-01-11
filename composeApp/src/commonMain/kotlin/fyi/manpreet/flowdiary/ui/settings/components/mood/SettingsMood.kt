package fyi.manpreet.flowdiary.ui.settings.components.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.settings_my_mood
import flowdiary.composeapp.generated.resources.settings_my_mood_subtitle
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionRow
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.settings.state.SettingsEvent
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsMood(
    modifier: Modifier = Modifier,
    emotions: List<Emotions>,
    onEmotionTypeUpdate: (SettingsEvent.EmotionUpdate) -> Unit,
) {

    if (emotions.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = MaterialTheme.spacing.medium),
    ) {

        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            Text(
                text = stringResource(Res.string.settings_my_mood),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(Res.string.settings_my_mood_subtitle),
                modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        EmotionRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.medium)
                .padding(horizontal = MaterialTheme.spacing.small),
            emotions = emotions,
            onClick = { onEmotionTypeUpdate(SettingsEvent.EmotionUpdate(it)) },
        )
    }
}
