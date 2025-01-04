package fyi.manpreet.flowdiary.platform.audiorecord

import fyi.manpreet.flowdiary.usecase.MainActivityUseCase

actual class AudioRecorder(
    private val mainActivityUseCase: MainActivityUseCase,
) {

    actual suspend fun startRecording(fileName: String) {

    }

    actual suspend fun stopRecording(): String {
        return ""
    }

    actual fun isRecording(): Boolean = false

}
