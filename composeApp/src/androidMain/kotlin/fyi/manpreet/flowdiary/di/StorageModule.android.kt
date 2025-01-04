package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.data.model.AudioTable
import  fyi.manpreet.flowdiary.platform.storage.StorageManager
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideStorageModule() = module {
    single<KStore<AudioTable>> {
        val storageManager: StorageManager = get()
        val path = storageManager.getStorageDir()
        storeOf(file = path, default = AudioTable())
    }
}

actual fun provideStorageManager() = module {
    singleOf(::StorageManager)
}
