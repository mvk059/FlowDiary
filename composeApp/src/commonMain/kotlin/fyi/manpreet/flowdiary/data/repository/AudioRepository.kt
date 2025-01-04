package fyi.manpreet.flowdiary.data.repository

import fyi.manpreet.flowdiary.data.model.Audio

interface AudioRepository {

    suspend fun insertRecording(recording: Audio)

    suspend fun getRecordingById(id: Long): Audio?

    suspend fun getAllRecordings(): List<Audio>

    suspend fun deleteRecordings(recordings: List<Audio>)
}
