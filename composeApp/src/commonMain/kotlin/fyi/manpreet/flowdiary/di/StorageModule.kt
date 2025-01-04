package fyi.manpreet.flowdiary.di

import org.koin.core.module.Module

expect fun provideStorageManager(): Module

expect fun provideStorageModule(): Module