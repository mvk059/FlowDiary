package fyi.manpreet.flowdiary.ui.components.emotion

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Stable
data class Emotions(
    val type: EmotionType,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val isSelected: Boolean = false,
    val contentDescription: StringResource,
)