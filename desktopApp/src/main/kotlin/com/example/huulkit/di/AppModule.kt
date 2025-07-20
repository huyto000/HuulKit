package com.example.huulkit.di

import com.example.huulkit.ai.GeminiService
import com.example.huulkit.ai.TranslationService
import com.example.huulkit.data.repository.ConfigRepositoryImpl
import com.example.huulkit.data.repository.TextRefinementRepositoryImpl
import com.example.huulkit.data.source.ConfigDataSource
import com.example.huulkit.data.source.ConfigDataSourceImpl
import com.example.huulkit.data.source.TextRefinementDataSource
import com.example.huulkit.data.source.TextRefinementDataSourceImpl
import com.example.huulkit.weather.WeatherService
import com.example.huulkit.domain.repository.ConfigRepository
import com.example.huulkit.domain.repository.TextRefinementRepository
import com.example.huulkit.domain.usecase.GetGeminiApiKeyUseCase
import com.example.huulkit.domain.usecase.GetWeatherApiKeyUseCase
import com.example.huulkit.domain.usecase.RefineTextUseCase
import com.example.huulkit.domain.usecase.UpdateGeminiApiKeyUseCase
import com.example.huulkit.domain.usecase.UpdateWeatherApiKeyUseCase
import com.example.huulkit.presentation.viewmodel.ConfigViewModel
//import com.example.huulkit.presentation.viewmodel.MainViewModel
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {
    // Coroutine scope
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // Data sources
    single<ConfigDataSource> { ConfigDataSourceImpl() }

    // Repositories
    single<ConfigRepository> { ConfigRepositoryImpl(get()) }

    // Use cases
    single { GetGeminiApiKeyUseCase(get()) }
    single { UpdateGeminiApiKeyUseCase(get()) }
    single { GetWeatherApiKeyUseCase(get()) }
    single { UpdateWeatherApiKeyUseCase(get()) }

    // Gemini Chat Model - depends on ConfigRepository
    single {
        GoogleAiGeminiChatModel.builder()
            .apiKey(get<ConfigRepository>().getGeminiApiKey())
            .modelName("gemini-2.0-flash")
            .temperature(1.0)
            .build()
    }

    // Services - depends on GoogleAiGeminiChatModel
    single { GeminiService() }
    single { TranslationService() }

    // Weather Service
    single { WeatherService(get()) }

    // Data sources - depends on GeminiService
    single<TextRefinementDataSource> { TextRefinementDataSourceImpl() }

    // Repositories - depends on TextRefinementDataSource
    single<TextRefinementRepository> { TextRefinementRepositoryImpl(get()) }

    // Use cases - depends on TextRefinementRepository
    single { RefineTextUseCase(get()) }

    // ViewModels
//    single { MainViewModel() }
    single { ConfigViewModel(get(), get(), get(), get()) }
    single { TextRefinementViewModel(get(), get()) }
    single { TranslatorViewModel(get(), get()) }
}
