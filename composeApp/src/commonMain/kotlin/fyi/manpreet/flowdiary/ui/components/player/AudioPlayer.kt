package fyi.manpreet.flowdiary.ui.components.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import fyi.manpreet.flowdiary.data.model.AmplitudeData
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.util.formatDuration
import fyi.manpreet.flowdiary.util.getBackgroundColor
import fyi.manpreet.flowdiary.util.getIconColor
import fyi.manpreet.flowdiary.util.noRippleClickable
import kotlin.time.Duration

@Composable
fun AudioPlayer(
    modifier: Modifier = Modifier,
    amplitudeData: List<AmplitudeData>,
    isPlaying: Boolean,
    emotionType: EmotionType,
    currentPosition: Duration, // in seconds
    totalDuration: Duration, // in seconds
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

            WaveformSeekbar(
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .padding(horizontal = 4.dp),
                amplitudes = amplitudeData,
                progress = progress,
                onSeek = { },
                progressColor = Color(0xFF2962FF),
                backgroundColor = Color.LightGray
            )

            // TODO Fix spacing
            Row(
                modifier = Modifier.widthIn(min = 16.dp),
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
fun WaveformSeekbar(
    modifier: Modifier = Modifier,
    amplitudes: List<AmplitudeData>,
    progress: Float,
    onSeek: (Float) -> Unit,
    progressColor: Color = Color(0xFF2962FF),
    backgroundColor: Color = Color.White,
) {
    val touchPosition = remember { mutableStateOf<Float?>(null) }

    // Cache path objects to reduce object creation
    val upperPath = remember { Path() }
    val lowerPath = remember { Path() }

    Canvas(
        modifier = modifier
            .height(24.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { touchPosition.value = it.x },
                    onDragEnd = {
                        touchPosition.value?.let { pos ->
                            onSeek(pos / size.width)
                        }
                        touchPosition.value = null
                    },
                    onDragCancel = { touchPosition.value = null },
                    onDrag = { change, _ ->
                        touchPosition.value = change.position.x.coerceIn(0f, size.width.toFloat())
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        val barWidth = (width / amplitudes.size) * 0.7f
        val spacing = (width / amplitudes.size) * 0.3f
        val maxBarHeight = height / 2
        val cornerRadius = (barWidth * 0.5f).coerceAtMost(8.dp.toPx())

        // Helper function to draw a bar segment
        fun drawBarSegment(x: Float, barHeight: Float, isUpper: Boolean, color: Color) {
            val path = if (isUpper) upperPath else lowerPath
            path.reset()

            if (isUpper) {
                // Upper bar with rounded top corners
                path.moveTo(x, centerY)
                path.lineTo(x, centerY - barHeight + cornerRadius)
                path.arcTo(
                    Rect(
                        left = x,
                        top = centerY - barHeight,
                        right = x + cornerRadius * 2,
                        bottom = centerY - barHeight + cornerRadius * 2
                    ),
                    180f,
                    90f,
                    false
                )
                path.lineTo(x + barWidth - cornerRadius, centerY - barHeight)
                path.arcTo(
                    Rect(
                        left = x + barWidth - cornerRadius * 2,
                        top = centerY - barHeight,
                        right = x + barWidth,
                        bottom = centerY - barHeight + cornerRadius * 2
                    ),
                    270f,
                    90f,
                    false
                )
                path.lineTo(x + barWidth, centerY)
            } else {
                // Lower bar with rounded bottom corners
                path.moveTo(x, centerY)
                path.lineTo(x, centerY + barHeight - cornerRadius)
                path.arcTo(
                    Rect(
                        left = x,
                        top = centerY + barHeight - cornerRadius * 2,
                        right = x + cornerRadius * 2,
                        bottom = centerY + barHeight
                    ),
                    180f,
                    -90f,
                    false
                )
                path.lineTo(x + barWidth - cornerRadius, centerY + barHeight)
                path.arcTo(
                    Rect(
                        left = x + barWidth - cornerRadius * 2,
                        top = centerY + barHeight - cornerRadius * 2,
                        right = x + barWidth,
                        bottom = centerY + barHeight
                    ),
                    90f,
                    -90f,
                    false
                )
                path.lineTo(x + barWidth, centerY)
            }
            path.close()

            drawPath(path = path, color = color)
        }

        amplitudes.forEachIndexed { index, amplitude ->
            val x = index * (barWidth + spacing)
            val barHeight = amplitude.amplitude * maxBarHeight
            val color = if (x / width <= progress) progressColor else backgroundColor

            // Draw upper and lower segments
            drawBarSegment(x, barHeight, true, color)
            drawBarSegment(x, barHeight, false, color)
        }

        // Draw progress indicator
        touchPosition.value?.let { pos ->
            drawCircle(
                color = progressColor,
                radius = 6.dp.toPx(),
                center = Offset(pos, centerY)
            )
        }
    }
}
