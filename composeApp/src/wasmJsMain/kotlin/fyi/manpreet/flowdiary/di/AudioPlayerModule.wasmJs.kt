package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.platform.audioplayer.AudioPlayer
import org.koin.dsl.module

actual fun provideAudioPlayerModule() = module {
    single { AudioPlayer("audio-player") }
}
