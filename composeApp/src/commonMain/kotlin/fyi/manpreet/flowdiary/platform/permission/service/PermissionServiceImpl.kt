package fyi.manpreet.flowdiary.platform.permission.service

import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.PermissionState
import fyi.manpreet.flowdiary.platform.permission.delegate.PermissionDelegate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

internal class PermissionServiceImpl: PermissionService, KoinComponent {

    override suspend fun checkPermission(permission: Permission): PermissionState = try {
        getPermissionDelegate(permission).getPermissionState()
    } catch (e: Exception) {
        e.printStackTrace()
        PermissionState.NOT_DETERMINED
    }

    override fun checkPermissionFlow(permission: Permission): Flow<PermissionState> = flow {
        while (true) {
            val permissionState = getPermissionDelegate(permission).getPermissionState()
            emit(permissionState)
            delay(1000L)
        }
    }

    override suspend fun providePermission(permission: Permission) = try {
        getPermissionDelegate(permission).providePermission()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun openSettingsPage(permission: Permission) = try {
        getPermissionDelegate(permission).openSettingPage()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

internal fun KoinComponent.getPermissionDelegate(permission: Permission): PermissionDelegate {
    val permissionDelegate by inject<PermissionDelegate>(named(permission.name))
    return permissionDelegate
}