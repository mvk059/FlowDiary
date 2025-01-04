package fyi.manpreet.flowdiary.platform.storage

import fyi.manpreet.flowdiary.usecase.MainActivityUseCase
import fyi.manpreet.flowdiary.util.Constants
import kotlinx.io.files.Path

actual class StorageManager(
    private val mainActivityUseCase: MainActivityUseCase,
) {
    actual fun getStorageDir() =
        Path("${mainActivityUseCase.requireActivity().filesDir.path}${Constants.STORAGE_FILE_NAME}")
}
