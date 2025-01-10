package fyi.manpreet.flowdiary.platform.audiorecord

import android.media.MediaRecorder
import android.os.Build
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.log10

actual class AudioRecorder(
    private val mainActivityUseCase: MainActivityUseCase,
) {

    private var mediaRecorder: MediaRecorder? = null
    private var isCurrentlyRecording = false
    private var isPausedState = false
    private var currentFilePath: String? = null
    private var amplitudesFile: File? = null
    private var amplitudeJob: Job? = null

    actual suspend fun startRecording(fileName: String) {
        val file = File(mainActivityUseCase.requireActivity().filesDir, fileName)
        currentFilePath = file.absolutePath
        amplitudesFile = File(file.parent, "${file.nameWithoutExtension}.amplitudes")

        mediaRecorder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(mainActivityUseCase.requireActivity())
            else MediaRecorder()

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(currentFilePath)
            prepare()
            start()
        }
        isCurrentlyRecording = true
        amplitudeJob?.cancel()
        amplitudeJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive && isRecording()) {
                if (!isPaused()) {
                    val amplitude = mediaRecorder?.maxAmplitude?.toFloat() ?: 0f
                    if (amplitude > 0) {
                        val normalized = (log10(amplitude + 1) / log10(32767f + 1)).coerceIn(0f, 1f)
                        amplitudesFile?.appendText("$normalized,")
                    }
                }
                delay(100)
            }
        }
    }

    // TODO Raise
    actual suspend fun stopRecording(): Pair<String, String> {
        amplitudeJob?.cancel()
        mediaRecorder?.apply {
            stop()  // File is already saved by this point
            release()
        }
        mediaRecorder = null
        isCurrentlyRecording = false
        val amplitudesFilePath = amplitudesFile?.absolutePath

        requireNotNull(currentFilePath) { "Current file path is null" }
        requireNotNull(amplitudesFilePath) { "amplitudesFile file path is null" }

        return currentFilePath!! to amplitudesFilePath
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
        amplitudeJob?.cancel()

        // Delete the file if it exists
        currentFilePath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
            if (amplitudesFile?.exists() == true) {
                amplitudesFile?.delete()
            }
        }
        currentFilePath = null
    }

    actual fun isPaused(): Boolean = isPausedState

}
