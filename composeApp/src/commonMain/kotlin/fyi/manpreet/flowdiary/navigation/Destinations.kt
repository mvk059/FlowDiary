package fyi.manpreet.flowdiary.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeDestination

@Serializable
data class NewRecordDestination(val audioRecordingPath: String, val audioAmplitudePath: String)

@Serializable
object SettingsDestination