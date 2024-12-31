package fyi.manpreet.flowdiary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Gradients(
    val default: Brush = GradientDefault,
    val pressed: Brush = GradientPressed,
    val favorite: Brush = GradientFavorite,
    val selection: Brush = GradientSelection,
    val borderButton: Brush = GradientButtonBorder,
    val bottomScreenShadow: Brush = GradientBottomScreenShadow,
    val topScreenShadow: Brush = GradientTopScreenShadow,
) {

    private companion object {

        val GradientDefault = Brush.linearGradient(
            colors = listOf(PrimaryContainer, PurpleMedium1)
        )

        val GradientPressed = Brush.linearGradient(
            colors = listOf(PurpleLight2, PurpleMedium2)
        )

        val GradientFavorite = Brush.linearGradient(
            colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.8f to Color.Transparent,
                1.0f to Color(0xFF332b28)
            ),
        )

        val GradientSelection = Brush.linearGradient(
            colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.8f to Color.Transparent,
                1.0f to Color(0xFF332b28)
            ),
            start = Offset(0f, Offset.Infinite.y),
            end = Offset(Offset.Infinite.x, 0f),
        )

        val GradientButtonBorder = Brush.linearGradient(
            colors = listOf(PrimaryContainer.copy(0.5f), PurpleMedium1.copy(0.5f))
        )

        val GradientBottomScreenShadow = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color.Transparent,
                1.0f to Color(0xFF141218)
            ),
        )

        val GradientTopScreenShadow = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color.Transparent,
                1.0f to Color(0xFF141218)
            ),
            startY = Float.POSITIVE_INFINITY,
            endY = 0f,
        )
    }
}

val LocalGradient = staticCompositionLocalOf { Gradients() }

val MaterialTheme.gradient: Gradients
    @Composable @ReadOnlyComposable get() = LocalGradient.current