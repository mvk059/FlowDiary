package fyi.manpreet.flowdiary.ui.home.state

data class AudioDragRecordState(
    val isDragging: Boolean = false,
    val dragOffset: Float = 0f,
    val isInCancelZone: Boolean = false
)