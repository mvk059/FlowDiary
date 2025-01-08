package fyi.manpreet.flowdiary.platform.audioplayer

import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.darwin.NSObjectProtocol
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

actual class AudioPlayer {
    private var player: AVPlayer? = null
    private var onPlaybackComplete: (() -> Unit)? = null
    private var playerItemObserver: NSObjectProtocol? = null

    @OptIn(ExperimentalForeignApi::class)
    actual fun play(path: String) {
        try {
            release()
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, null)
            audioSession.setActive(true, null)
            val nsUrl = formatUrl(path)
            player = AVPlayer.playerWithURL(nsUrl)

            // Set up notification observer for playback completion
            setupPlaybackObserver()

            player?.play()
        } catch (e: Exception) {
            Logger.e { "Audio playback error: ${e.message}" }
        }
    }

    actual fun release() {
        removePlaybackObserver()
        player?.pause()
        player = null
    }

    actual fun stop() {
        player?.pause()
    }

    actual fun setOnPlaybackCompleteListener(listener: () -> Unit) {
        onPlaybackComplete = listener
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun getAudioDuration(filePath: String): Duration {
        val url = formatUrl(filePath)
        val asset = AVURLAsset(url, null)
        val durationMs =  (CMTimeGetSeconds(asset.duration) * 1000).toLong()
        return durationMs.milliseconds
    }

    private fun formatUrl(url: String): NSURL {
        return when {
            url.startsWith("file://") || url.startsWith("http") -> NSURL(string = url)
            else -> NSURL.fileURLWithPath(url)
        }
    }

    private fun setupPlaybackObserver() {
        removePlaybackObserver() // Remove any existing observer

        playerItemObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = player?.currentItem,
            queue = null
        ) { _ ->
            onPlaybackComplete?.invoke()
        }
    }

    private fun removePlaybackObserver() {
        playerItemObserver?.let { observer ->
            NSNotificationCenter.defaultCenter.removeObserver(observer)
            playerItemObserver = null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun getCurrentPosition(): Duration {
        return player?.let {
            val seconds = CMTimeGetSeconds(it.currentTime())
            (seconds * 1000).toLong().milliseconds
        } ?: Duration.ZERO
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekTo(position: Duration) {
        player?.let {
            val seconds = position.inWholeMilliseconds / 1000.0
            val time = CMTimeMakeWithSeconds(seconds, 1000)
            it.seekToTime(time)
        }
    }
}
