package fyi.manpreet.flowdiary.platform.audiorecord

actual class AudioRecorder {
    actual suspend fun startRecording(fileName: String) {
    }

    actual suspend fun stopRecording(): String {
        TODO("Not yet implemented")
    }

    actual fun isRecording(): Boolean {
        TODO("Not yet implemented")
    }
}