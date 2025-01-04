package fyi.manpreet.flowdiary.data.repository

import fyi.manpreet.flowdiary.data.datasource.AudioLocalDatasource
import fyi.manpreet.flowdiary.data.mapper.toAudio
import fyi.manpreet.flowdiary.data.mapper.toAudioData
import fyi.manpreet.flowdiary.data.model.Audio

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
}
