package fyi.manpreet.flowdiary.data.repository

import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic

interface AudioRepository {

    suspend fun insertRecording(recording: Audio)

    suspend fun getRecordingById(id: Long): Audio?

    suspend fun getAllRecordings(): List<Audio>

    suspend fun deleteRecordings(recordings: List<Audio>)

    suspend fun getDefaultEmotion(): EmotionType?

    suspend fun setDefaultEmotion(emotionType: EmotionType)

    suspend fun getAllSelectedTopics(): Set<Topic>

    suspend fun insertSelectedTopic(topic: Topic)

    suspend fun removeSelectedTopic(topic: Topic)

    suspend fun getAllSavedTopics(): Set<Topic>

    suspend fun insertSavedTopic(topic: Topic)
}
