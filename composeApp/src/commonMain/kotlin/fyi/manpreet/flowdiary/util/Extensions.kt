package fyi.manpreet.flowdiary.util

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import com.composables.core.SheetDetent
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_excited
import flowdiary.composeapp.generated.resources.ic_neutral
import flowdiary.composeapp.generated.resources.ic_peaceful
import flowdiary.composeapp.generated.resources.ic_sad
import flowdiary.composeapp.generated.resources.ic_stressed
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Recordings
import fyi.manpreet.flowdiary.ui.theme.Excited35
import fyi.manpreet.flowdiary.ui.theme.Excited95
import fyi.manpreet.flowdiary.ui.theme.Neutral35
import fyi.manpreet.flowdiary.ui.theme.Neutral95
import fyi.manpreet.flowdiary.ui.theme.Peaceful35
import fyi.manpreet.flowdiary.ui.theme.Peaceful95
import fyi.manpreet.flowdiary.ui.theme.Sad35
import fyi.manpreet.flowdiary.ui.theme.Sad95
import fyi.manpreet.flowdiary.ui.theme.Stressed35
import fyi.manpreet.flowdiary.ui.theme.Stressed95
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import kotlin.time.Duration

val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
    containerHeight * 0.3f
}

@Composable
fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        interactionSource = interactionSource,
        indication = indication,
    ) {
        onClick()
    }
}

fun List<Audio>.toRecordingList(): List<Recordings> {
    return this
        .groupBy { audio ->
            Instant.fromEpochMilliseconds(audio.createdDateInMillis)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.toString()
        }
        .entries
        .sortedByDescending { it.key }
        .flatMap { (date, audioList) ->
            listOf(
                Recordings.Date(date),
                Recordings.Entry(audioList)
            )
        }
}

fun Duration.formatDuration(): String {
    val seconds = (inWholeSeconds % 60).toString().padStart(2, '0')
    val minutes = (inWholeMinutes % 60).toString().padStart(2, '0')
    val hours = inWholeHours.toString().padStart(2, '0')
    val wholeHours = inWholeHours

    return when {
        wholeHours > 0 -> "$hours:$minutes:$seconds"
        else -> "$minutes:$seconds"
    }
}

fun EmotionType.getDrawableResource(): DrawableResource {
    return when (this) {
        EmotionType.Excited -> Res.drawable.ic_excited
        EmotionType.Peaceful -> Res.drawable.ic_peaceful
        EmotionType.Neutral -> Res.drawable.ic_neutral
        EmotionType.Sad -> Res.drawable.ic_sad
        EmotionType.Stressed -> Res.drawable.ic_stressed
    }
}

fun EmotionType.getBackgroundColor(): Color {
    return when (this) {
        EmotionType.Excited -> Excited95
        EmotionType.Peaceful -> Peaceful95
        EmotionType.Neutral -> Neutral95
        EmotionType.Sad -> Sad95
        EmotionType.Stressed -> Stressed95
    }
}

fun EmotionType.getIconColor(): Color {
    return when (this) {
        EmotionType.Excited -> Excited35
        EmotionType.Peaceful -> Peaceful35
        EmotionType.Neutral -> Neutral35
        EmotionType.Sad -> Sad35
        EmotionType.Stressed -> Stressed35
    }
}
