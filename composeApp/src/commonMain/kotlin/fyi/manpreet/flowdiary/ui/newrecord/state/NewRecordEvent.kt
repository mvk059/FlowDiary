package fyi.manpreet.flowdiary.ui.newrecord.state

sealed interface NewRecordEvent {
    data class UpdateTitle(val title: String) : NewRecordEvent
    data class UpdateTopic(val topic: String) : NewRecordEvent
    data class UpdateDescription(val description: String) : NewRecordEvent
    data object Cancel : NewRecordEvent
    data object Save : NewRecordEvent
}
