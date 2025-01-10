package fyi.manpreet.flowdiary.platform.audiorecord

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.AVFAudio.AVAudioQualityHigh
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVEncoderAudioQualityKey
import platform.AVFAudio.AVEncoderBitRateKey
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileHandle
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.seekToEndOfFile
import platform.Foundation.writeData
import platform.Foundation.writeToFile
import platform.posix.pow

actual class AudioRecorder {

    private var audioRecorder: AVAudioRecorder? = null
    private var currentUrl: NSURL? = null
    private var amplitudesUrl: NSURL? = null
    private var isPausedState = false
    private var amplitudeJob: Job? = null
    private var fileHandle: NSFileHandle? = null

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun startRecording(fileName: String) {
        val fileManager = NSFileManager.defaultManager
        val documentsPath = (fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL)

        currentUrl = documentsPath?.URLByAppendingPathComponent(fileName)

        // Create amplitudes file path
        val amplitudesFileName = fileName.substringBeforeLast(".") + ".amplitudes"
        amplitudesUrl = documentsPath?.URLByAppendingPathComponent(amplitudesFileName)

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
            put(AVEncoderBitRateKey, 128000)
            put("AVEnableRecordingMetering" as Any?, true)
        }

        audioRecorder = AVAudioRecorder(currentUrl!!, settings, null)
        audioRecorder?.prepareToRecord()
        audioRecorder?.meteringEnabled = true
        audioRecorder?.record()

        // Create empty amplitude file
        amplitudesUrl?.path?.let { path ->
            NSString.create(string = "")
                .writeToFile(path, true, NSUTF8StringEncoding, null)

            // Open file handle for appending
            fileHandle = NSFileHandle.fileHandleForWritingAtPath(path)
            fileHandle?.seekToEndOfFile()
        }

        // Start recording amplitudes
        amplitudeJob?.cancel()
        amplitudeJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive && isRecording()) {
                if (!isPaused()) {
                    val normalized = getCurrentAmplitude()
                    if (normalized > 0) {
                        amplitudesUrl?.path?.let { path ->
                            withContext(Dispatchers.IO) {
                                val normalisedString = "$normalized,"
                                val data = normalisedString.encodeToByteArray()
                                data.usePinned { pinned ->
                                    fileHandle?.writeData(
                                        NSData.create(
                                            bytes = pinned.addressOf(0),
                                            length = data.size.toULong()
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                delay(100)
            }
        }
    }

    actual suspend fun stopRecording(): Pair<String, String> {
        amplitudeJob?.cancel()
        audioRecorder?.stop()
        val path = currentUrl?.path ?: throw IllegalStateException("Recording was not started")
        val amplitudesPath =
            amplitudesUrl?.path ?: throw IllegalStateException("Amplitudes file not created")

        audioRecorder = null
        currentUrl = null
        amplitudesUrl = null

        return path to amplitudesPath
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

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun discardRecording() {
        amplitudeJob?.cancel()
        audioRecorder?.stop()
        audioRecorder = null
        isPausedState = false

        // Delete both files if they exist
        val fileManager = NSFileManager.defaultManager
        currentUrl?.path?.let { path ->
            fileManager.removeItemAtPath(path, null)
        }
        amplitudesUrl?.path?.let { path ->
            fileManager.removeItemAtPath(path, null)
        }
        currentUrl = null
        amplitudesUrl = null
    }

    actual fun isPaused(): Boolean = isPausedState

    private fun getCurrentAmplitude(): Float {
        audioRecorder?.let { recorder ->
            recorder.updateMeters()

            // Get the peak power from both channels
            val channel0Power = recorder.peakPowerForChannel(0u)
            val channel1Power = recorder.peakPowerForChannel(1u)

            // Use the maximum of both channels
            val maxPower = maxOf(channel0Power, channel1Power)

            // Convert dB to normalized value (0-1)
            return if (maxPower.isFinite()) {
                val normalized = pow(10.0, maxPower / 20.0).toFloat()
                normalized.coerceIn(0f, 1f)
            } else {
                0f
            }
        }
        return 0f
    }
}