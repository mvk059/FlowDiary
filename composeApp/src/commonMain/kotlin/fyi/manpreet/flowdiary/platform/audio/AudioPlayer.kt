package fyi.manpreet.flowdiary.platform.audio

expect class AudioPlayer {
    fun play(url: String)
    fun stop()
    fun release()
}