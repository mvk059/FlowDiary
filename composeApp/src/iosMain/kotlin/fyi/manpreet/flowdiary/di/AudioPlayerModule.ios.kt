package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.platform.audio.AudioPlayer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideAudioPlayerModule() = module {
    singleOf(::AudioPlayer)
}
