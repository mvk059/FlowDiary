package fyi.manpreet.flowdiary.platform.filemanager

import arrow.core.raise.Raise
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase
import java.io.File

actual class FileManager(
    private val mainActivityUseCase: MainActivityUseCase,
) {
    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String {
        return try {
            val filesDir = mainActivityUseCase.requireActivity().filesDir
            File(filesDir, relativePath).absolutePath
        } catch (e: Exception) {
            raise("Failed to get file path: ${e.message}")
        }
    }
}