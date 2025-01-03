package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.platform.permission.delegate.PermissionDelegate
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun providePermissionsModule() = module {
    single<PermissionDelegate>(named(Permission.AUDIO.name)) {
        PermissionDelegate(get())
    }
}