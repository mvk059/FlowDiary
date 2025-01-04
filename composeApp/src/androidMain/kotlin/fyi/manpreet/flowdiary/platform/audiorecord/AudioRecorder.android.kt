package fyi.manpreet.flowdiary.platform.audiorecord

import android.media.MediaRecorder
import android.os.Build
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase
import java.io.File

actual class AudioRecorder(
    private val mainActivityUseCase: MainActivityUseCase,
) {

    private var mediaRecorder: MediaRecorder? = null
    private var isCurrentlyRecording = false
    private var isPausedState = false
    private var currentFilePath: String? = null

    actual suspend fun startRecording(fileName: String) {
        val file = File(mainActivityUseCase.requireActivity().filesDir, fileName)
        currentFilePath = file.absolutePath

        mediaRecorder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(mainActivityUseCase.requireActivity())
            else MediaRecorder()

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(currentFilePath)
            prepare()
            start()
        }
        isCurrentlyRecording = true
    }

    // TODO Raise
    actual suspend fun stopRecording(): String {
        mediaRecorder?.apply {
            stop()  // File is already saved by this point
            release()
        }
        mediaRecorder = null
        isCurrentlyRecording = false
        return currentFilePath ?: throw IllegalStateException("Recording was not started")
    }

    actual fun isRecording(): Boolean = isCurrentlyRecording

    actual suspend fun pauseRecording() {
        mediaRecorder?.pause()
        isPausedState = true
    }

    actual suspend fun resumeRecording() {
        mediaRecorder?.resume()
        isPausedState = false
    }

    actual suspend fun discardRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isCurrentlyRecording = false
        isPausedState = false

        // Delete the file if it exists
        currentFilePath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
        currentFilePath = null
    }

    actual fun isPaused(): Boolean = isPausedState

}
