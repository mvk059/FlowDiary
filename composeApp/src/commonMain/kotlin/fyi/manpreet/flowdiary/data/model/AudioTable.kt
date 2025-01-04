package fyi.manpreet.flowdiary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AudioTable(
    val recordings: List<AudioData> = emptyList(),
) {

    @Serializable
    data class AudioData(
        val id: Long = 0L,
        val imageUrl: String,
        val createdDateInMillis: Long,
        val path: String,
    )
}
