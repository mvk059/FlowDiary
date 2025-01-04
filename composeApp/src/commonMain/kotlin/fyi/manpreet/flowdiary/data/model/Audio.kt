package fyi.manpreet.flowdiary.data.model

import kotlin.jvm.JvmInline

data class Audio(
    val id: Long = INVALID_ID,
    val path: AudioPath? = null,
    val createdDateInMillis: Long ,
) {

    companion object {
        const val INVALID_ID = 0L
    }
}

@JvmInline
value class AudioPath(val value: String)
