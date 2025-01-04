package fyi.manpreet.flowdiary.platform.audiorecord

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioQualityHigh
import platform.AVFAudio.AVAudioRecorder
import platform.Foundation.*
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVEncoderAudioQualityKey
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC

actual class AudioRecorder {

    private var audioRecorder: AVAudioRecorder? = null
    private var currentUrl: NSURL? = null
    private var isPausedState = false

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun startRecording(fileName: String) {
        val fileManager = NSFileManager.defaultManager
        val documentsPath = (fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL)

        currentUrl = documentsPath?.URLByAppendingPathComponent(fileName)

        // Configure audio session
        val audioSession = AVAudioSession.sharedInstance()
        audioSession.setCategory("AVAudioSessionCategoryPlayAndRecord", null)
        audioSession.setActive(true, null)

        // Setup recording settings
        val settings = mutableMapOf<Any?, Any>().apply {
            put(AVFormatIDKey, kAudioFormatMPEG4AAC)
            put(AVSampleRateKey, 44100.0)
            put(AVNumberOfChannelsKey, 2)
            put(AVEncoderAudioQualityKey, AVAudioQualityHigh)
        }

        audioRecorder = AVAudioRecorder(currentUrl!!, settings, null)
        audioRecorder?.prepareToRecord()
        audioRecorder?.record()
    }

    actual suspend fun stopRecording(): String {
        audioRecorder?.stop()
        val path = currentUrl?.path ?: throw IllegalStateException("Recording was not started")
        audioRecorder = null
        currentUrl = null
        return path
    }

    actual fun isRecording(): Boolean = audioRecorder?.recording == true

    actual suspend fun pauseRecording() {
        audioRecorder?.pause()
        isPausedState = true
    }

    actual suspend fun resumeRecording() {
        audioRecorder?.record()
        isPausedState = false
    }

    // In iosMain
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun discardRecording() {
        audioRecorder?.stop()
        audioRecorder = null
        isPausedState = false

        // Delete the file if it exists
        currentUrl?.path?.let { path ->
            val fileManager = NSFileManager.defaultManager
            fileManager.removeItemAtPath(path, null)
        }
        currentUrl = null
    }

    actual fun isPaused(): Boolean  = isPausedState
}