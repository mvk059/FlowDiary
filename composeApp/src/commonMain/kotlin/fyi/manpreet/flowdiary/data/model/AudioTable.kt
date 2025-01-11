package fyi.manpreet.flowdiary.data.model

import fyi.manpreet.flowdiary.ui.home.state.Topic
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class AudioTable(
    val recordings: List<AudioData> = emptyList(),
    val defaultTopics: Set<Topic> = emptySet(),
    val savedTopics: Set<Topic> = emptySet(),
    val defaultEmotionType: String? = null,
) {

    @Serializable
    data class AudioData(
        val id: Long = 0L,
        val createdDateInMillis: Long,
        val path: String,
        val amplitudePath: String,
        val title: String,
        val emotionType: String,
        val topics: List<String>,
        val description: String,
        val duration: Duration,
        val amplitudeData: List<AmplitudeData>
    )
}