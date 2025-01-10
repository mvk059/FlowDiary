package fyi.manpreet.flowdiary.data.datasource

import fyi.manpreet.flowdiary.data.model.AudioTable

interface AudioLocalDatasource {

    suspend fun insertRecording(audio: AudioTable.AudioData)

    suspend fun getRecordingById(id: Long): AudioTable.AudioData?

    suspend fun getAllRecordings(): List<AudioTable.AudioData>

    suspend fun deleteRecordings(recordings: List<AudioTable.AudioData>)

}
