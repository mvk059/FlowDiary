package fyi.manpreet.flowdiary.platform.environment

import fyi.manpreet.flowdiary.BuildConfig

actual class Environment {
    actual fun getApiKey(): String = BuildConfig.GROQ
}