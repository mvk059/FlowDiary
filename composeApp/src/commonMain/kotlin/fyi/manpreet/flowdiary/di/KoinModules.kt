package fyi.manpreet.flowdiary.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import fyi.manpreet.flowdiary.ui.home.HomeViewModel

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideViewModelModule,
        )
    }

val provideViewModelModule = module {
    viewModelOf(::HomeViewModel)
}