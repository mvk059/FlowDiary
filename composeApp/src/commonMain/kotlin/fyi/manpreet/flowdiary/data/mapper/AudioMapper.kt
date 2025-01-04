package fyi.manpreet.flowdiary.data.mapper

import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.data.model.AudioTable
import kotlinx.datetime.Clock

fun Audio.toAudioData(): AudioTable.AudioData {
    val path = this.path
    requireNotNull(path) { "Path cannot be null" }

    return AudioTable.AudioData(
        id = id,
        createdDateInMillis =
            if (createdDateInMillis == Audio.INVALID_ID) Clock.System.now().toEpochMilliseconds()
            else createdDateInMillis,
        path = path.value.substringAfterLast("/"),
    )
}

fun List<Audio>.toAudioData() =
    map { meme -> meme.toAudioData() }

fun AudioTable.AudioData.toAudio(): Audio {
    return Audio(
        id = id,
        path = AudioPath(path),
        createdDateInMillis = createdDateInMillis,
    )
}

fun List<AudioTable.AudioData>.toAudio(): List<Audio> {
    return this.map { memeTable ->
        memeTable.toAudio()
    }
}
