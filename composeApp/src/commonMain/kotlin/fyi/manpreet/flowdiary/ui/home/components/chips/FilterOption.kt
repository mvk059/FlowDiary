package fyi.manpreet.flowdiary.ui.home.components.chips

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.DrawableResource

@Stable
data class FilterOption(
    val title: String,
    val options: List<Options>,
) {

    @Stable
    data class Options(
        val id: Int,
        val text: String,
        val icon: DrawableResource,
        val isSelected: Boolean = false,
    )
}