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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.fab_cd
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun GradientFAB(
    modifier: Modifier = Modifier,
    onRecordStop: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.buttonPressed
        else MaterialTheme.gradient.button

    Box(
        modifier = modifier.size(MaterialTheme.spacing.large5XL)
    ) {
        // Second level (larger outer gradient shadow)
        Box(
            modifier = Modifier
                .size(MaterialTheme.spacing.large5XL)
                .align(Alignment.Center)
                .background(color = Color(0xFFEEF0FF), shape = CircleShape)
                .alpha(0.3f) // Make it more transparent for shadow effect
        )

        // First level (smaller inner gradient shadow)
        Box(
            modifier = Modifier
                .size(MaterialTheme.spacing.large4XL)
                .align(Alignment.Center)
                .background(color = Color(0xFFD9E2FF), shape = CircleShape)
//                .alpha(0.6f) // Less transparent than outer shadow
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
                    onClick = onRecordStop,
                ),
            contentAlignment = Alignment.Center,
        ) {

            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(Res.string.fab_cd),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}