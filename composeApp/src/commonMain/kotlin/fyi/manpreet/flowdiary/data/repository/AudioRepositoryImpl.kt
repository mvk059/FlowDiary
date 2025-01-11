package fyi.manpreet.flowdiary.data.repository

import fyi.manpreet.flowdiary.data.datasource.AudioLocalDatasource
import fyi.manpreet.flowdiary.data.mapper.toAudio
import fyi.manpreet.flowdiary.data.mapper.toAudioData
import fyi.manpreet.flowdiary.data.model.Audio
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.home.state.Topic

class AudioRepositoryImpl(
    private val localDataSource: AudioLocalDatasource,
) : AudioRepository {
    override suspend fun insertRecording(recording: Audio) {
        localDataSource.insertRecording(recording.toAudioData())
    }

    override suspend fun getRecordingById(id: Long): Audio? = localDataSource.getRecordingById(id)?.toAudio()


    override suspend fun getAllRecordings(): List<Audio> = localDataSource.getAllRecordings().toAudio()


    override suspend fun deleteRecordings(recordings: List<Audio>) {
        localDataSource.deleteRecordings(recordings.toAudioData())
    }

    override suspend fun getDefaultEmotion(): EmotionType? = localDataSource.getDefaultEmotion()

    override suspend fun setDefaultEmotion(emotionType: EmotionType) {
        localDataSource.setDefaultEmotion(emotionType)
    }

    override suspend fun getAllSelectedTopics(): Set<Topic> = localDataSource.getAllSelectedTopics()

    override suspend fun insertSelectedTopic(topic: Topic) {
        localDataSource.insertSelectedTopic(topic = topic)
    }

    override suspend fun removeSelectedTopic(topic: Topic) {
        localDataSource.removeSelectedTopic(topic = topic)
    }

    override suspend fun getAllSavedTopics(): Set<Topic> = localDataSource.getAllSavedTopics()

    override suspend fun insertSavedTopic(topic: Topic) {
        localDataSource.insertSavedTopic(topic = topic)
    }
}
