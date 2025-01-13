package fyi.manpreet.flowdiary.di

import fyi.manpreet.flowdiary.data.datasource.AudioLocalDatasource
import fyi.manpreet.flowdiary.data.datasource.AudioLocalDatasourceImpl
import fyi.manpreet.flowdiary.data.repository.AudioRepository
import fyi.manpreet.flowdiary.data.repository.AudioRepositoryImpl
import fyi.manpreet.flowdiary.platform.environment.Environment
import fyi.manpreet.flowdiary.platform.permission.service.PermissionService
import fyi.manpreet.flowdiary.platform.permission.service.PermissionServiceImpl
import fyi.manpreet.flowdiary.ui.home.HomeViewModel
import fyi.manpreet.flowdiary.ui.newrecord.NewRecordViewModel
import fyi.manpreet.flowdiary.ui.settings.SettingsViewModel
import io.github.vyfor.groqkt.GroqClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideViewModelModule,
            providePermissionModule,
            provideDataSourceModule,
            provideRepositoryModule,
            provideNetworkModule,
            provideAudioPlayerModule(),
            providePermissionsModule(),
            provideAudioRecordModule(),
            provideStorageManager(),
            provideStorageModule(),
            provideFileManagerModule(),
            provideEnvironmentModule(),
        )
    }

val provideViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewRecordViewModel)
    viewModelOf(::SettingsViewModel)
}

private val providePermissionModule = module {
    singleOf(::PermissionServiceImpl) bind PermissionService::class
}

val provideDataSourceModule = module {
    singleOf(::AudioLocalDatasourceImpl).bind(AudioLocalDatasource::class)
}

val provideRepositoryModule = module {
    singleOf(::AudioRepositoryImpl).bind(AudioRepository::class)
}

val provideNetworkModule = module {
    single { createJson() }
    single { createHttpClient(get(), true) }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

fun createHttpClient(json: Json, enableNetworkLogs: Boolean) = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    if (enableNetworkLogs) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.NONE
        }
    }
}

fun createGroqClient(environment: Environment): GroqClient {
    return GroqClient(apiKey = environment.getApiKey())
}