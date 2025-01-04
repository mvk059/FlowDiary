package fyi.manpreet.flowdiary.platform.filemanager

import arrow.core.raise.Raise

expect class FileManager {
    suspend fun Raise<String>.getFullImagePath(relativePath: String): String
}
