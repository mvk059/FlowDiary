package fyi.manpreet.flowdiary.platform.filemanager

import arrow.core.raise.Raise
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class FileManager {
    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String {
        val fileManager = NSFileManager.defaultManager
        val documentDirectory = (fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL) ?: raise("Failed to get document directory")
        return documentDirectory.URLByAppendingPathComponent(relativePath)?.path
            ?: raise("Failed to get file path")
    }
}