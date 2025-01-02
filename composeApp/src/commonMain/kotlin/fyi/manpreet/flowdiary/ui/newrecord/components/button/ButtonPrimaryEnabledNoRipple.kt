package fyi.manpreet.flowdiary.ui.newrecord.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.util.noRippleClickable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowScope.ButtonPrimaryEnabledNoRipple(
    modifier: Modifier = Modifier,
    text: StringResource,
    weight: Float,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.buttonPressed
        else MaterialTheme.gradient.button

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight
            )
            .background(
                brush = gradient,
                shape = MaterialTheme.shapes.large
            )
            .align(Alignment.CenterVertically)
            .weight(weight)
            .noRippleClickable(
                interactionSource = interactionSource,
                onClick = onClick
            )
    ) {
        Text(
            text = stringResource(text),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
        )
    }
}