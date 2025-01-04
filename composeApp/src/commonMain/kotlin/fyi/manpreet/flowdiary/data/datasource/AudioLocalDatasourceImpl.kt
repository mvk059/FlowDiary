package fyi.manpreet.flowdiary.data.datasource

import fyi.manpreet.flowdiary.data.model.AudioTable
import io.github.xxfast.kstore.KStore

class AudioLocalDatasourceImpl(
    private val storage: KStore<AudioTable>,
//    private val fileManager: FileManager,
) : AudioLocalDatasource {
    override suspend fun insertRecording(meme: AudioTable.AudioData) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecordingById(id: Long): AudioTable.AudioData? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRecordings(): List<AudioTable.AudioData> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecordings(memes: List<AudioTable.AudioData>) {
        TODO("Not yet implemented")
    }
}