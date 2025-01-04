package fyi.manpreet.flowdiary.platform.filemanager

import arrow.core.raise.Raise

actual class FileManager {
    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String = ""
}