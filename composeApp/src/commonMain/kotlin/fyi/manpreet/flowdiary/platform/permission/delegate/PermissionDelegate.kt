package fyi.manpreet.flowdiary.platform.permission.delegate

import fyi.manpreet.flowdiary.platform.permission.PermissionState

expect class PermissionDelegate {
    suspend fun getPermissionState(): PermissionState
    suspend fun providePermission()
    fun openSettingPage()
}
