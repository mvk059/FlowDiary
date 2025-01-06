package fyi.manpreet.flowdiary.data.model

import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic
import kotlin.jvm.JvmInline

data class Audio(
    val id: Long = INVALID_ID,
    val path: AudioPath? = null,
    val createdDateInMillis: Long,
    val title: String,
    val emotionType: EmotionType,
    val topics: List<Topic>,
    val description: String,
    val isPlaying: Boolean,
) {

    companion object {
        const val INVALID_ID = 0L
    }
}

@JvmInline
value class AudioPath(val value: String)
