package fyi.manpreet.flowdiary.platform.filemanager

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import fyi.manpreet.flowdiary.data.model.AmplitudeData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringWithContentsOfFile

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

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun Raise<String>.getAmplitudeData(path: String): List<AmplitudeData> {
        return withContext(Dispatchers.Default) {
            catch(
                catch = { raise(it.message ?: "Unknown error loading amplitude data") },
                block = {
                    val fileManager = NSFileManager.defaultManager
                    ensure(fileManager.fileExistsAtPath(path)) { "File not found" }

                    // Read file content as string
                    val nsString = NSString.stringWithContentsOfFile(
                        path,
                        encoding = NSUTF8StringEncoding,
                        error = null
                    ) ?: raise("Could not read file")

                    // Split into lines and process
                    nsString
                        .lines()
                        .flatMap { line ->
                            line.split(",")
                        }
                        .mapNotNull { value ->
                            if (value.isNotEmpty()) AmplitudeData(value.toFloat()) else null
                        }
                }
            )
        }
    }
}