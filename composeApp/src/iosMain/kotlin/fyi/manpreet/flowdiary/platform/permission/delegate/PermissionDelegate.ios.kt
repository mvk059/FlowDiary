package fyi.manpreet.flowdiary.platform.permission.delegate

import co.touchlab.kermit.Logger
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaType
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO Use Raise
actual class PermissionDelegate {
    actual suspend fun getPermissionState(): PermissionState {
        return when (val status: AVAuthorizationStatus = currentAuthorizationStatus()) {
            AVAuthorizationStatusAuthorized -> PermissionState.GRANTED
            AVAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
            AVAuthorizationStatusDenied -> PermissionState.DENIED
            AVAuthorizationStatusRestricted -> PermissionState.GRANTED
            else -> {
                Logger.e { "unknown authorization status $status" }
                PermissionState.NOT_DETERMINED
            }
        }
    }

    actual suspend fun providePermission() {
        when (val status: AVAuthorizationStatus = currentAuthorizationStatus()) {
            AVAuthorizationStatusAuthorized -> return
            AVAuthorizationStatusNotDetermined -> {
                val isGranted: Boolean = suspendCoroutine { continuation ->
                    AVCaptureDevice.requestAccess(AVMediaTypeAudio) { continuation.resume(it) }
                }
                if (isGranted) return
                else Logger.e { "unknown authorization status $status" }
            }

            AVAuthorizationStatusDenied -> Logger.e { "unknown authorization status $status" }
            else -> {
                Logger.e { "unknown authorization status $status" }
            }
        }
    }

    private fun currentAuthorizationStatus(): AVAuthorizationStatus {
        return AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio)
    }

    actual fun openSettingPage() {
        val settingsUrl = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        settingsUrl?.let {
            UIApplication.sharedApplication.openURL(settingsUrl, mapOf<Any?, Any>(), null)
        }
    }
}

private fun AVCaptureDevice.Companion.requestAccess(
    type: AVMediaType,
    callback: (isGranted: Boolean) -> Unit
) {
    this.requestAccessForMediaType(type, mainContinuation { isGranted: Boolean ->
        callback(isGranted)
    })
}