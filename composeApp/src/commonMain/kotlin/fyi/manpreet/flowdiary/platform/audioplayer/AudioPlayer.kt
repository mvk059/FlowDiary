package fyi.manpreet.flowdiary.platform.audioplayer

expect class AudioPlayer {
    fun play(url: String)
    fun stop()
    fun release()
    fun setOnPlaybackCompleteListener(listener: () -> Unit)
}