package fyi.manpreet.flowdiary.ui.home.components.chips

import org.jetbrains.compose.resources.DrawableResource

data class FilterOption(
    val title: String,
    val options: List<Options>
) {

    data class Options(
        val id: Int,
        val text: String,
        val icon: DrawableResource,
        val isSelected: Boolean = false,
    )
}