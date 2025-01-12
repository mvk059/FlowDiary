package fyi.manpreet.flowdiary.ui.components.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fyi.manpreet.flowdiary.data.model.AmplitudeData
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.formatDuration
import fyi.manpreet.flowdiary.util.getBackgroundColor
import fyi.manpreet.flowdiary.util.getIconColor
import fyi.manpreet.flowdiary.util.getPlaybackBackgroundColor
import fyi.manpreet.flowdiary.util.noRippleClickable
import kotlin.time.Duration

@Composable
fun AudioPlayer(
    modifier: Modifier = Modifier,
    amplitudeData: List<AmplitudeData>,
    isPlaying: Boolean,
    emotionType: EmotionType,
    currentPosition: Duration,
    totalDuration: Duration,
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
) {

    val formattedTime by remember(currentPosition) { derivedStateOf { currentPosition.formatDuration() } }
    val progress by remember(currentPosition, totalDuration) {
        derivedStateOf {
            (currentPosition.inWholeMilliseconds.toFloat() /
                    totalDuration.inWholeMilliseconds.toFloat()).coerceIn(0f, 1f)
        }
    }

    Column(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(MaterialTheme.spacing.large))
            .background(emotionType.getBackgroundColor())
            .padding(MaterialTheme.spacing.extraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play/Pause Button
            Box(
                modifier = Modifier
                    .size(MaterialTheme.spacing.largeXL)
                    .background(Color.White, CircleShape)
                    .padding(MaterialTheme.spacing.extraSmall)
                    .noRippleClickable { onPlayPauseClick() }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = emotionType.getIconColor()
                )
            }

            WaveformSeekbar2(
                modifier = Modifier
                    .weight(1f)
                    .height(MaterialTheme.spacing.large)
                    .padding(horizontal = MaterialTheme.spacing.extraSmall),
                amplitudes = amplitudeData,
                progress = progress,
                onSeek = { },
                progressColor = emotionType.getIconColor(),
                backgroundColor = emotionType.getPlaybackBackgroundColor()
            )

            Row(
                modifier = Modifier
                    .widthIn(min = MaterialTheme.spacing.medium)
                    .padding(end = MaterialTheme.spacing.extraSmall),
            ) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "/",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = totalDuration.formatDuration(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

        }
    }
}

@Composable
fun WaveformSeekbar2(
    modifier: Modifier = Modifier,
    amplitudes: List<AmplitudeData>,
    progress: Float,
    onSeek: (Float) -> Unit,
    progressColor: Color,
    backgroundColor: Color
) {
    val minBarCount = 25 // Minimum number of bars to display

    Canvas(
        modifier = modifier
            .height(MaterialTheme.spacing.large)
            .fillMaxWidth()
    ) {
        if (amplitudes.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val centerY = height / 2

        // Calculate dynamic bar count based on available width
        val minBarWidth = 5.dp.toPx()
        val desiredSpacing = 3.dp.toPx()
        val maxPossibleBars = (width / (minBarWidth + desiredSpacing)).toInt()

        // Use the larger of minBarCount or maxPossibleBars
        val barCount = maxOf(minBarCount, maxPossibleBars)

        // Calculate actual bar width and spacing
        val totalBarWidth = width / barCount
        val barWidth = totalBarWidth * 0.7f  // 70% of space for bar
        val spacing = totalBarWidth * 0.3f    // 30% of space for spacing
        val cornerRadius = 12.dp.toPx()

        // Resample amplitudes to match desired bar count
        val sampledAmplitudes = when {
            amplitudes.size < barCount -> {
                // Interpolate up
                List(barCount) { index ->
                    val position = index.toFloat() * (amplitudes.size - 1) / (barCount - 1)
                    val lowIndex = position.toInt()
                    val highIndex = (lowIndex + 1).coerceAtMost(amplitudes.size - 1)
                    val fraction = position - lowIndex

                    val interpolatedAmplitude = lerp(
                        amplitudes[lowIndex].amplitude,
                        amplitudes[highIndex].amplitude,
                        fraction
                    )
                    AmplitudeData(interpolatedAmplitude)
                }
            }

            amplitudes.size > barCount -> {
                // Use peak values when down sampling
                val windowSize = amplitudes.size / barCount
                amplitudes.windowed(
                    size = windowSize,
                    step = windowSize,
                    partialWindows = true
                ) { window ->
                    // Take the peak value in each window instead of average
                    AmplitudeData(window.maxOf { it.amplitude })
                }.take(barCount)
            }

            else -> amplitudes
        }

        // Draw the bars with smoother animation
        sampledAmplitudes.forEachIndexed { index, amplitude ->
            val x = index * (barWidth + spacing)
            val barHeight = (amplitude.amplitude * height).coerceIn(1.dp.toPx(), height)
            val y = centerY - (barHeight / 2)

            val color = if (x / width <= progress) progressColor else backgroundColor

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + fraction * (end - start)
}
