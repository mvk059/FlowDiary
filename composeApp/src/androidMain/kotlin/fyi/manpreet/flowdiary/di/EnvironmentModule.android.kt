package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.platform.environment.Environment
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideEnvironmentModule() = module {
    singleOf(::Environment)
}