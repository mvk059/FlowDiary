package fyi.manpreet.flowdiary.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import fyi.manpreet.flowdiary.ui.home.HomeViewModel
import fyi.manpreet.flowdiary.ui.newrecord.NewRecordViewModel
import fyi.manpreet.flowdiary.ui.settings.SettingsViewModel

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideViewModelModule,
        )
    }

val provideViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewRecordViewModel)
    viewModelOf(::SettingsViewModel)
}