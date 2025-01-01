package fyi.manpreet.flowdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_excited
import flowdiary.composeapp.generated.resources.ic_neutral
import flowdiary.composeapp.generated.resources.ic_peaceful
import flowdiary.composeapp.generated.resources.ic_sad
import flowdiary.composeapp.generated.resources.ic_stressed
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _moodChip = MutableStateFlow<FilterOption?>(null)
    val moodChip = _moodChip
        .onStart { initMoodChip() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val _topicsChip = MutableStateFlow<FilterOption?>(null)
    val topicsChip = _topicsChip
        .onStart { initTopicChip() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Chip.MoodChip -> onMoodChipSelect(event.id)
            is HomeEvent.Chip.TopicChip -> onTopicChipSelect(event.id)
            HomeEvent.Chip.MoodReset -> onMoodChipReset()
            HomeEvent.Chip.TopicReset -> onTopicChipReset()
        }
    }

    private fun initMoodChip() {
        val filterOption = FilterOption(
            title = "All Moods",
            options = listOf(
                FilterOption.Options(id = 1, text = "Excited", icon = Res.drawable.ic_excited),
                FilterOption.Options(id = 2, text = "Peaceful", icon = Res.drawable.ic_peaceful),
                FilterOption.Options(id = 3, text = "Neutral", icon = Res.drawable.ic_neutral),
                FilterOption.Options(id = 4, text = "Sad", icon = Res.drawable.ic_sad),
                FilterOption.Options(id = 5, text = "Stressed", icon = Res.drawable.ic_stressed),
            )
        )
        _moodChip.update { filterOption }
    }

    private fun initTopicChip() {
        val filterOption = FilterOption(
            title = "All Topics",
            options = listOf(),
        )
        _topicsChip.update { filterOption }
    }

    private fun onMoodChipSelect(id: Int) {
        val moodChip = _moodChip.value
        require(moodChip != null) { "Mood chip can not be null." }

        val options = moodChip.options.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }

        _moodChip.update { it?.copy(options = options) }
    }

    private fun onTopicChipSelect(id: Int) {
        val topicsChip = _topicsChip.value
        require(topicsChip != null) { "Topic chip can not be null." }

        val options = topicsChip.options.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }

        _topicsChip.update { it?.copy(options = options) }
    }

    private fun onMoodChipReset() {
        val moodChip = _moodChip.value
        require(moodChip != null) { "Mood chip can not be null." }

        val options = moodChip.options.map { it.copy(isSelected = false) }
        _moodChip.update { it?.copy(options = options) }
    }

    private fun onTopicChipReset() {
        val topicsChip = _topicsChip.value
        require(topicsChip != null) { "Topic chip can not be null." }

        val options = topicsChip.options.map { it.copy(isSelected = false) }
        _topicsChip.update { it?.copy(options = options) }
    }

}