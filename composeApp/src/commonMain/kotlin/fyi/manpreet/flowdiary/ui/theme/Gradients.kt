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
) {

    private companion object {

        val GradientBackground = Brush.verticalGradient(
            colors = listOf(Secondary90, Secondary95)
//            colors = listOf(Color.Red, Color.Cyan)
        )
    }
}

val LocalGradient = staticCompositionLocalOf { Gradients() }

val MaterialTheme.gradient: Gradients
    @Composable @ReadOnlyComposable get() = LocalGradient.current