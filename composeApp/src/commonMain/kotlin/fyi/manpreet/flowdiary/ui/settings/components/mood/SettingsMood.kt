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
import flowdiary.composeapp.generated.resources.settings_my_mood
import flowdiary.composeapp.generated.resources.settings_my_mood_subtitle
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionRow
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

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

@Composable
fun SettingsMood(
    modifier: Modifier = Modifier,
) {

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
            onClick = {},
        )
    }
}
