package fyi.manpreet.flowdiary.ui.home.state

import androidx.compose.runtime.Stable
import kotlin.time.Duration

@Stable
data class PlaybackState(
    val playingId: Long?,
    val position: Duration,
    val isSeeking: Boolean = false,
) {
    companion object {
        val NotPlaying = PlaybackState(null, Duration.ZERO)
        const val DEFAULT_ID = 1L
    }
}