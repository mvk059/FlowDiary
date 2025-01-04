package fyi.manpreet.flowdiary.platform.permission.delegate

import android.Manifest
import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase

actual class PermissionDelegate(
    private val mainActivityUseCase: MainActivityUseCase
) {
    actual suspend fun getPermissionState(): PermissionState {
        return checkPermissions(
            mainActivityUseCase.requireActivity(),
            mainActivityUseCase.requireActivity(),
            recordAudioPermissions,
        )
    }

    actual suspend fun providePermission() {
        mainActivityUseCase.requireActivity().providePermissions(recordAudioPermissions) {
            throw IllegalStateException("Cannot provide permission: ${Permission.MICROPHONE.name}")
        }
    }

    actual fun openSettingPage() {
        mainActivityUseCase.requireActivity().openAppSettingsPage(Permission.MICROPHONE)
    }

    private val recordAudioPermissions: List<String> = listOf(Manifest.permission.RECORD_AUDIO)

}