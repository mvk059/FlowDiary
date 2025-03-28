package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.platform.filemanager.FileManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideFileManagerModule() = module {
    singleOf(::FileManager)
}