package fyi.manpreet.flowdiary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun FlowTheme(
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalFixedAccentColors provides getFixedAccentColors,
        LocalSpacing provides Spacing(),
        LocalGradient provides Gradients(),
    ) {

        MaterialTheme(
            colorScheme = ColorScheme,
            typography = Typography(),
            shapes = Shapes,
            content = content,
        )
    }
}
