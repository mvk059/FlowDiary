package fyi.manpreet.flowdiary.data.datasource

import fyi.manpreet.flowdiary.data.model.AudioTable
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic

interface AudioLocalDatasource {

    suspend fun insertRecording(audio: AudioTable.AudioData)

    suspend fun getRecordingById(id: Long): AudioTable.AudioData?

    suspend fun getAllRecordings(): List<AudioTable.AudioData>

    suspend fun deleteRecordings(recordings: List<AudioTable.AudioData>)

    suspend fun getDefaultEmotion(): EmotionType?

    suspend fun setDefaultEmotion(emotionType: EmotionType)

    suspend fun getAllSelectedTopics(): Set<Topic>

    suspend fun insertSelectedTopic(topic: Topic)

    suspend fun removeSelectedTopic(topic: Topic)

    suspend fun getAllSavedTopics(): Set<Topic>

    suspend fun insertSavedTopic(topic: Topic)
}
