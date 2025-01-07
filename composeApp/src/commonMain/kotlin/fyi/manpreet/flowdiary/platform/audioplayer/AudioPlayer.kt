package fyi.manpreet.flowdiary.platform.audioplayer

import kotlin.time.Duration

expect class AudioPlayer {
    fun play(path: String)
    fun stop()
    fun release()
    fun setOnPlaybackCompleteListener(listener: () -> Unit)
    fun getAudioDuration(filePath: String): Duration
}