package fyi.manpreet.flowdiary.ui.newrecord.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fyi.manpreet.flowdiary.util.noRippleClickable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowScope.ButtonDisabledNoRipple(
    modifier: Modifier = Modifier,
    text: StringResource,
    weight: Float,
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.large
            )
            .align(Alignment.CenterVertically)
            .weight(weight)
            .noRippleClickable { onClick() },
    ) {
        Text(
            text = stringResource(text),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
        )
    }
}