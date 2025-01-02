package fyi.manpreet.flowdiary.ui.home.components.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
    val gradient =
        if (isPressed) MaterialTheme.gradient.buttonPressed
        else MaterialTheme.gradient.button

    val backgroundSecondLevel = if (isPlaying) Color(0xFFEEF0FF) else Color.Transparent
    val backgroundFirstLevel = if (isPlaying) Color(0xFFD9E2FF) else Color.Transparent
    val icon = if (isPlaying) Icons.Default.Done else Icons.Default.Mic

    Box(
        modifier = modifier.size(MaterialTheme.spacing.large6XL)
    ) {
        // Second level (larger outer gradient shadow)
        Box(
            modifier = Modifier
                .size(MaterialTheme.spacing.large6XL)
                .align(Alignment.Center)
                .background(color = backgroundSecondLevel, shape = CircleShape)
        )

        // First level (smaller inner gradient shadow)
        Box(
            modifier = Modifier
                .size(MaterialTheme.spacing.large5XL)
                .align(Alignment.Center)
                .background(color = backgroundFirstLevel, shape = CircleShape)
        )

        // Main FAB
        Box(
            modifier = modifier
                .size(MaterialTheme.spacing.large3XL)
                .align(Alignment.Center)
                .background(brush = gradient, shape = CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) {

            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = icon,
                contentDescription = stringResource(Res.string.fab_cd),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}