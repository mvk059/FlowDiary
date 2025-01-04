package fyi.manpreet.flowdiary.di

import  fyi.manpreet.flowdiary.platform.storage.StorageManager
import fyi.manpreet.flowdiary.data.model.AudioTable
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideStorageManager() = module {
    single<KStore<AudioTable>> {
        storeOf("memes", AudioTable())
    }
}

actual fun provideStorageModule() = module {
    singleOf(::StorageManager)
}