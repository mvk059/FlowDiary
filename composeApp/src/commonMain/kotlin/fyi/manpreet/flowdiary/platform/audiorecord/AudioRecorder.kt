package fyi.manpreet.flowdiary.platform.audiorecord

expect class AudioRecorder {
    suspend fun startRecording(fileName: String)
    suspend fun stopRecording(): String  // Returns the file path
    fun isRecording(): Boolean
}