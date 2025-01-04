package fyi.manpreet.flowdiary.platform.storage

import fyi.manpreet.flowdiary.util.Constants
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class StorageManager {

    @OptIn(ExperimentalForeignApi::class)
    actual fun getStorageDir(): Path {
        val fileManager: NSFileManager = NSFileManager.defaultManager

        val documentDirectory = (fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL)?.path ?: throw IllegalStateException("Could not get documents directory")

        val dirPath = Path("$documentDirectory/${Constants.STORAGE_FILE_NAME}")

        // Ensure directory exists
        if (!fileManager.fileExistsAtPath(documentDirectory)) {
            fileManager.createDirectoryAtPath(
                documentDirectory,
                true,
                null,
                null
            )
        }

        return dirPath
    }
}
