package fyi.manpreet.flowdiary.data.datasource

import fyi.manpreet.flowdiary.data.model.AudioTable

interface AudioLocalDatasource {

    suspend fun insertRecording(meme: AudioTable.AudioData)

    suspend fun getRecordingById(id: Long): AudioTable.AudioData?

    suspend fun getAllRecordings(): List<AudioTable.AudioData>

    suspend fun deleteRecordings(memes: List<AudioTable.AudioData>)

}
