package fyi.manpreet.flowdiary.data.mapper

import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.data.model.AudioTable
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic
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
        title = title,
        topics = topics.toTopicsString(),
        description = description,
        emotionType = emotionType.toEmotionType(),
    )
}

fun List<Audio>.toAudioData() =
    map { meme -> meme.toAudioData() }

fun AudioTable.AudioData.toAudio(): Audio {
    val emotionType = emotionType.toEmotionType()
    requireNotNull(emotionType) { "emotionType type is null" }
    return Audio(
        id = id,
        path = AudioPath(path),
        createdDateInMillis = createdDateInMillis,
        title = title,
        topics = topics.toTopics(),
        description = description,
        emotionType = emotionType
    )
}

fun List<AudioTable.AudioData>.toAudio(): List<Audio> {
    return this.map { memeTable ->
        memeTable.toAudio()
    }
}

fun List<String>.toTopics(): List<Topic> {
    return this.map { topic -> Topic(topic) }
}

fun List<Topic>.toTopicsString(): List<String> {
    return this.map { it.value }
}

fun EmotionType.toEmotionType(): String {
    return when (this) {
        EmotionType.Excited -> "Excited"
        EmotionType.Peaceful -> "Peaceful"
        EmotionType.Neutral -> "Neutral"
        EmotionType.Sad -> "Sad"
        EmotionType.Stressed -> "Stressed"
    }
}

fun String.toEmotionType(): EmotionType? {
    return when (this) {
        "Excited" -> EmotionType.Excited
        "Peaceful" -> EmotionType.Peaceful
        "Neutral" -> EmotionType.Neutral
        "Sad" -> EmotionType.Sad
        "Stressed" -> EmotionType.Stressed
        else -> null
    }
}