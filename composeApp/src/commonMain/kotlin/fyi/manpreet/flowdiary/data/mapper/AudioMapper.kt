package fyi.manpreet.flowdiary.data.mapper

import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_excited
import flowdiary.composeapp.generated.resources.ic_neutral
import flowdiary.composeapp.generated.resources.ic_peaceful
import flowdiary.composeapp.generated.resources.ic_sad
import flowdiary.composeapp.generated.resources.ic_stressed
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.data.model.AudioTable
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.DrawableResource

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
        duration = duration,
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
        emotionType = emotionType,
        duration = duration,
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

fun EmotionType?.toIcon(): DrawableResource {
    return when (this) {
        EmotionType.Excited -> Res.drawable.ic_excited
        EmotionType.Peaceful -> Res.drawable.ic_peaceful
        EmotionType.Neutral -> Res.drawable.ic_neutral
        EmotionType.Sad -> Res.drawable.ic_sad
        EmotionType.Stressed -> Res.drawable.ic_stressed
        else -> Res.drawable.ic_excited
    }
}