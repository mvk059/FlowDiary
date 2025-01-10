package fyi.manpreet.flowdiary.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class AudioTable(
    val recordings: List<AudioData> = emptyList(),
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