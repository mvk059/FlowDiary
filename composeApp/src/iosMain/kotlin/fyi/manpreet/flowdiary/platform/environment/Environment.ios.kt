package fyi.manpreet.flowdiary.platform.environment

actual class Environment {
    actual fun getApiKey(): String =
        platform.Foundation.NSProcessInfo.processInfo.environment["groq"] as String
}