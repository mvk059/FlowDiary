package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.data.datasource.AudioLocalDatasource
import fyi.manpreet.flowdiary.data.datasource.AudioLocalDatasourceImpl
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.data.repository.AudioRepositoryImpl
import fyi.manpreet.flowdiary.platform.permission.service.PermissionService
import fyi.manpreet.flowdiary.platform.permission.service.PermissionServiceImpl
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import fyi.manpreet.flowdiary.ui.home.HomeViewModel
import fyi.manpreet.flowdiary.ui.newrecord.NewRecordViewModel
import fyi.manpreet.flowdiary.ui.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideViewModelModule,
            providePermissionModule,
            provideDataSourceModule,
            provideRepositoryModule,
            provideAudioPlayerModule(),
            providePermissionsModule(),
            provideAudioRecordModule(),
            provideStorageManager(),
            provideStorageModule(),
        )
    }

val provideViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewRecordViewModel)
    viewModelOf(::SettingsViewModel)
}

private val providePermissionModule = module {
    singleOf(::PermissionServiceImpl) bind PermissionService::class
}

val provideDataSourceModule = module {
    singleOf(::AudioLocalDatasourceImpl).bind(AudioLocalDatasource::class)
}

val provideRepositoryModule = module {
    singleOf(::AudioRepositoryImpl).bind(AudioRepository::class)
}
