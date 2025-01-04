package fyi.manpreet.flowdiary.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeDestination

@Serializable
data class NewRecordDestination(val path: String)

@Serializable
object SettingsDestination