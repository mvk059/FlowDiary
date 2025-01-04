package fyi.manpreet.flowdiary.platform.audiorecord

expect class AudioRecorder {
    suspend fun startRecording(fileName: String)
    suspend fun stopRecording(): String  // Returns the file path
    suspend fun pauseRecording()
    suspend fun resumeRecording()
    suspend fun discardRecording()
    fun isRecording(): Boolean
    fun isPaused(): Boolean
}