package fyi.manpreet.flowdiary.platform.storage

import fyi.manpreet.flowdiary.util.Constants
import kotlinx.io.files.Path

actual class StorageManager {
    actual fun getStorageDir(): Path = Path(Constants.STORAGE_FILE_NAME)
}