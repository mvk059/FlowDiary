package fyi.manpreet.flowdiary.ui.components.emotion

import fyi.manpreet.flowdiary.ui.newrecord.components.bottomsheet.EmotionType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class Emotions(
    val type: EmotionType,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val isSelected: Boolean = false,
    val contentDescription: StringResource,
)