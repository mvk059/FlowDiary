package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.platform.audiorecord.AudioRecorder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun provideAudioRecordModule() = module {
    singleOf(::AudioRecorder)
}
