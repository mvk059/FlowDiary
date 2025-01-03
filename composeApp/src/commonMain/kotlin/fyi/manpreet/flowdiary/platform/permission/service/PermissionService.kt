package fyi.manpreet.flowdiary.platform.permission.service

import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import kotlinx.coroutines.flow.Flow

interface PermissionService {
    suspend fun checkPermission(permission: Permission): PermissionState
    fun checkPermissionFlow(permission: Permission): Flow<PermissionState>
    suspend fun providePermission(permission: Permission)
    fun openSettingsPage(permission: Permission)
}
