package fyi.manpreet.flowdiary.platform.permission.delegate

import fyi.manpreet.flowdiary.platform.permission.PermissionState

actual class PermissionDelegate {
    actual suspend fun getPermissionState(): PermissionState {
        TODO("Not yet implemented")
    }

    actual suspend fun providePermission() {
    }

    actual fun openSettingPage() {
    }
}