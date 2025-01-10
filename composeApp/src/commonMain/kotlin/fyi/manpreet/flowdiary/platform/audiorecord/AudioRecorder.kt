package fyi.manpreet.flowdiary.platform.audiorecord

expect class AudioRecorder {
    suspend fun startRecording(fileName: String)
    suspend fun stopRecording(): Pair<String, String>
    suspend fun pauseRecording()
    suspend fun resumeRecording()
    suspend fun discardRecording()
    fun isRecording(): Boolean
    fun isPaused(): Boolean
}