package fyi.manpreet.flowdiary.data.model

import androidx.compose.runtime.Stable
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.time.Duration

@Stable
data class Audio(
    val id: Long = INVALID_ID,
    val path: AudioPath? = null,
    val amplitudePath: String,
    val createdDateInMillis: Long,
    val title: String,
    val emotionType: EmotionType,
    val topics: List<Topic>,
    val description: String,
    val duration: Duration,
    val amplitudeData: List<AmplitudeData>,
) {

    companion object {
        const val INVALID_ID = 0L
    }
}

@JvmInline
value class AudioPath(val value: String)

@JvmInline
@Serializable
value class AmplitudeData(val amplitude: Float)