package fyi.manpreet.flowdiary.data.datasource

import arrow.core.raise.either
import fyi.manpreet.flowdiary.data.model.AudioTable
import fyi.manpreet.flowdiary.platform.filemanager.FileManager
import io.github.xxfast.kstore.KStore

class AudioLocalDatasourceImpl(
    private val storage: KStore<AudioTable>,
    private val fileManager: FileManager,
) : AudioLocalDatasource {
    override suspend fun insertRecording(meme: AudioTable.AudioData) {
        storage.update { state ->
            val newId = state?.recordings?.maxOfOrNull { it.id }?.plus(1) ?: 1L
            val newMeme = meme.copy(id = newId)
            state?.copy(recordings = state.recordings + newMeme)
        }
    }

    override suspend fun getRecordingById(id: Long): AudioTable.AudioData? {
        val recordings = storage.get()?.recordings ?: return null
        val recording = recordings.firstOrNull { it.id == id }
        return recording?.copy(path = getRecordingPath(recording))
    }

    override suspend fun getAllRecordings(): List<AudioTable.AudioData> {
        return storage.get()?.recordings?.map { it.copy(path = getRecordingPath(it)) } ?: emptyList()
    }

    override suspend fun deleteRecordings(memes: List<AudioTable.AudioData>) {
        storage.update { state ->
            state?.copy(
                recordings = state.recordings.filterNot { recording -> memes.any { it.id == recording.id } }
            )
        }
    }

    private suspend fun getRecordingPath(meme: AudioTable.AudioData): String {
        return either {
            with(fileManager) {
                this@either.getFullImagePath(meme.path)
            }
        }.fold(
            ifLeft = { println("Failed to get recording path: $it"); meme.path },
            ifRight = { return it }
        )
    }
}
