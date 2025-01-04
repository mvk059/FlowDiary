package fyi.manpreet.flowdiary.ui.newrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewRecordViewModel(
    private val repository: AudioRepository,
): ViewModel() {

    private val _recordingPath = MutableStateFlow<String?>(null)
    val recordingPath: StateFlow<String?> = _recordingPath.asStateFlow()

    private val _newRecordState = MutableStateFlow<Audio?>(null)
    val newRecordState: StateFlow<Audio?> = _newRecordState.asStateFlow()

    fun savePath(path: String) {
        _recordingPath.update { path }
    }

    fun onEvent(event: NewRecordEvent) {
        when (event) {
            NewRecordEvent.Cancel -> {}
            NewRecordEvent.Save -> {}
            is NewRecordEvent.UpdateDescription -> {}
            is NewRecordEvent.UpdateTitle -> {}
            is NewRecordEvent.UpdateTopic -> {}
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val data = _newRecordState.value
            requireNotNull(data) { "Data is null" }
            repository.insertRecording(data)
        }
    }
}