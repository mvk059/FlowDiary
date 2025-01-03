package fyi.manpreet.flowdiary.platform.permission.delegate

import android.Manifest
import android.os.Build
import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase

actual class PermissionDelegate(
    private val mainActivityUseCase: MainActivityUseCase
) {
    actual suspend fun getPermissionState(): PermissionState {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissions(
                mainActivityUseCase.requireActivity(),
                mainActivityUseCase.requireActivity(),
                recordAudioPermissions,
            )
        } else {
            PermissionState.GRANTED
        }
    }

    actual suspend fun providePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mainActivityUseCase.requireActivity().providePermissions(recordAudioPermissions) {
                throw IllegalStateException("Cannot provide permission: ${Permission.AUDIO.name}")
            }
        }
    }

    actual fun openSettingPage() {
        mainActivityUseCase.requireActivity().openAppSettingsPage(Permission.AUDIO)
    }

    private val recordAudioPermissions: List<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.RECORD_AUDIO)
        } else {
            emptyList()
        }
}