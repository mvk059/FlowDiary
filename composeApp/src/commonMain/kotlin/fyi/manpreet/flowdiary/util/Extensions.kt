package fyi.manpreet.flowdiary.util

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.composables.core.SheetDetent

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