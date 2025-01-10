package fyi.manpreet.flowdiary.platform.filemanager

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import fyi.manpreet.flowdiary.data.model.AmplitudeData
import fyi.manpreet.flowdiary.usecase.MainActivityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    actual suspend fun Raise<String>.getAmplitudeData(path: String): List<AmplitudeData> {
        return withContext(Dispatchers.IO) {
            catch(
                catch = { raise(it.message ?: "Unknown error loading amplitude data") },
                block = {
                    val amplitudesFile = File(path)
                    ensure(amplitudesFile.exists()) { "File not found" }

                    amplitudesFile.readLines()
                        .flatMap { line ->
                            line.splitToSequence(",")
                        }
                        .mapNotNull {
                            if (it.isNotEmpty())
                                AmplitudeData(it.toFloat())
                            else null
                        }
                }
            )
        }
    }
}