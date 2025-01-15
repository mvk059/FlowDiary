package fyi.manpreet.flowdiary.ui.home.components.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.fab_cd
import fyi.manpreet.flowdiary.ui.home.state.AudioDragRecordState
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import fyi.manpreet.flowdiary.ui.theme.FabShadow
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

@Composable
fun HomeFab(
    modifier: Modifier = Modifier,
    audioDragRecordState: AudioDragRecordState,
    onFabClick: (HomeEvent.FabBottomSheet) -> Unit,
    onAudioEvent: (HomeEvent.AudioDragRecorder) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.buttonPressed
        else MaterialTheme.gradient.button

    val cancelZoneGap = (-75).dp
    var cancelZoneGapPx: Float
    with(LocalDensity.current) {
        cancelZoneGapPx = 70.dp.toPx()
    }

    Box(modifier = modifier) {
        // Delete button
        AnimatedVisibility(
            visible = audioDragRecordState.isDragging,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.offset(x = cancelZoneGap)
        ) {
            Box(
                modifier = Modifier
                    .scale(if (audioDragRecordState.isInCancelZone) 1.5f else 1f)
                    .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                    .padding(MaterialTheme.spacing.extraSmall)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cancel Recording",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }

        Box(
            modifier = modifier
                .defaultMinSize(
                    minWidth = MaterialTheme.spacing.fabContainerWidth,
                    minHeight = MaterialTheme.spacing.fabContainerHeight,
                )
                .background(brush = gradient, shape = CircleShape)
                .shadow(
                    elevation = MaterialTheme.spacing.smallMedium,
                    spotColor = FabShadow,
                    ambientColor = FabShadow,
                    shape = CircleShape
                )
                .tapAndLongPressDraggable(
                    onTap = { onFabClick(HomeEvent.FabBottomSheet.FabClick) },
                    onDragStart = { onAudioEvent(HomeEvent.AudioDragRecorder.Record) },
                    onDrag = { offset ->
                        val isDragLeft = offset.x < 0
                        val isInCancelZone = isDragLeft && abs(offset.x) > cancelZoneGapPx
                        onAudioEvent(HomeEvent.AudioDragRecorder.Drag(offset.x, isInCancelZone))
                    },
                    onDragEnd = {
                        if (audioDragRecordState.isInCancelZone) {
                            onAudioEvent(HomeEvent.AudioDragRecorder.Cancel)
                        } else {
                            onAudioEvent(HomeEvent.AudioDragRecorder.Done)
                        }
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.fab_cd),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

fun Modifier.tapAndLongPressDraggable(
    onTap: () -> Unit = {},
    onDragStart: () -> Unit = {},
    onDrag: (offset: Offset) -> Unit = {},
    onDragEnd: (offset: Offset) -> Unit = {}
) = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val down = awaitFirstDown()
            val downTime = Clock.System.now().epochSeconds
            var dragStartPosition = down.position
            var currentPosition = dragStartPosition

            // Wait for long press or up
            val longPress = awaitLongPressOrCancellation(down.id)

            if (longPress != null) {
                // Long press detected
                onDragStart()

                // Handle drag
                do {
                    val event = awaitPointerEvent()
                    val dragEvent = event.changes.firstOrNull()

                    if (dragEvent != null && dragEvent.positionChanged()) {
                        currentPosition = dragEvent.position
                        val dragAmount = currentPosition - dragStartPosition
                        onDrag(dragAmount)
                        dragEvent.consume()
                    }
                } while (event.changes.any { it.pressed })

                onDragEnd(currentPosition - dragStartPosition)
            } else {
                // No long press, check if it was a quick tap
                val upEvent = currentEvent.changes.firstOrNull()
                if (upEvent != null) {
                    val upTime = Clock.System.now().epochSeconds
                    val pressDuration = upTime - downTime

                    // If the press was short enough, consider it a tap
                    if (pressDuration < viewConfiguration.longPressTimeoutMillis) {
                        onTap()
                    }
                }
            }
        }
    }
}
