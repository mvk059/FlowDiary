package fyi.manpreet.flowdiary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Gradients(
    val background: Brush = GradientBackground,
    val button: Brush = GradientButton,
    val buttonPressed: Brush = GradientButtonPressed,
) {

    private companion object {

        val GradientBackground = Brush.verticalGradient(
            colors = listOf(Secondary90, Secondary95)
        )

        val GradientButton = Brush.verticalGradient(
            colors = listOf(Primary60, Primary50)
        )

        val GradientButtonPressed = Brush.verticalGradient(
            colors = listOf(Primary60, Primary40)
        )
    }
}

val LocalGradient = staticCompositionLocalOf { Gradients() }

val MaterialTheme.gradient: Gradients
    @Composable @ReadOnlyComposable get() = LocalGradient.current