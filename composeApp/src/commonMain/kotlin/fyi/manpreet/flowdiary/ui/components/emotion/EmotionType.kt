package fyi.manpreet.flowdiary.ui.components.emotion

sealed interface EmotionType {
    data object Excited : EmotionType
    data object Peaceful : EmotionType
    data object Neutral : EmotionType
    data object Sad : EmotionType
    data object Stressed : EmotionType
}