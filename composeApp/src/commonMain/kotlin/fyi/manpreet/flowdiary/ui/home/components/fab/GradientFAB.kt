package fyi.manpreet.flowdiary.ui.home.components.fab

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.fab_cd
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun GradientFAB(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient = if (isPressed) MaterialTheme.gradient.buttonPressed
    else MaterialTheme.gradient.button

    val infiniteTransition = rememberInfiniteTransition()
    val firstCircleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val secondCircleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 100),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box {

        Canvas(
            modifier = modifier
                .size(MaterialTheme.spacing.large6XL)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        ) {
            if (isPlaying) {
                // Second level circle
                drawCircle(
                    color = Color(0xFFEEF0FF),
                    radius = (size.minDimension / 2) * secondCircleScale,
                    alpha = 0.5f
                )
                // First level circle
                drawCircle(
                    color = Color(0xFFD9E2FF),
                    radius = (size.minDimension / 2.2f) * firstCircleScale,
                    alpha = 0.7f
                )
            }

            // Main FAB
            drawCircle(
                brush = gradient,
                radius = size.minDimension / 3f
            )
        }

        // Icon overlay
        Box(
            modifier = Modifier
                .size(MaterialTheme.spacing.large3XL)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Done else Icons.Default.Mic,
                contentDescription = stringResource(Res.string.fab_cd),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
