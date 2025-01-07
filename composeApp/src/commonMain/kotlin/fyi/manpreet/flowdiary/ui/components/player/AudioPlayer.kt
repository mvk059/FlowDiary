package fyi.manpreet.flowdiary.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.util.getBackgroundColor
import fyi.manpreet.flowdiary.util.getIconColor
import fyi.manpreet.flowdiary.util.noRippleClickable

@Composable
fun AudioPlayer(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    emotionType: EmotionType,
    currentPosition: Float, // in seconds
    totalDuration: Float, // in seconds
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(24.dp))
            .background(emotionType.getBackgroundColor())
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play/Pause Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
                    .noRippleClickable { onPlayPauseClick() }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = emotionType.getIconColor()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = formatDuration(currentPosition),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "/",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatDuration(totalDuration),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AudioWaveformSeekbar(
    currentPosition: Float,
    totalDuration: Float,
    onSeek: (Float) -> Unit,
) {
    Slider(
        value = currentPosition,
        onValueChange = { newPosition ->
            onSeek(newPosition)
        },
        valueRange = 0f..totalDuration,
        modifier = Modifier
            .wrapContentSize()
    )
}

private fun formatDuration(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    return "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
}