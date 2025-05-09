package com.example.huulkit.di

import com.example.huulkit.data.repository.ConfigRepositoryImpl
import com.example.huulkit.data.repository.TextRefinementRepositoryImpl
import com.example.huulkit.data.source.ConfigDataSourceImpl
import com.example.huulkit.data.source.TextRefinementDataSourceImpl
import com.example.huulkit.domain.repository.ConfigRepository
import com.example.huulkit.domain.repository.TextRefinementRepository
import com.example.huulkit.domain.usecase.GetGeminiApiKeyUseCase
import com.example.huulkit.domain.usecase.RefineTextUseCase
import com.example.huulkit.domain.usecase.UpdateGeminiApiKeyUseCase
import com.example.huulkit.presentation.viewmodel.ConfigViewModel
import com.example.huulkit.presentation.viewmodel.MainViewModel
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Container for dependency injection
 */
object AppContainer {
    // Coroutine scope for the application
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Data sources
    private val configDataSource = ConfigDataSourceImpl()
    private val textRefinementDataSource = TextRefinementDataSourceImpl()

    // Repositories
    private val configRepository: ConfigRepository = ConfigRepositoryImpl(configDataSource)
    private val textRefinementRepository: TextRefinementRepository = TextRefinementRepositoryImpl(textRefinementDataSource)

    // Use cases
    private val getGeminiApiKeyUseCase = GetGeminiApiKeyUseCase(configRepository)
    private val updateGeminiApiKeyUseCase = UpdateGeminiApiKeyUseCase(configRepository)
    private val refineTextUseCase = RefineTextUseCase(textRefinementRepository)

    // View models
    val mainViewModel = MainViewModel()
    val configViewModel = ConfigViewModel(getGeminiApiKeyUseCase, updateGeminiApiKeyUseCase)
    val textRefinementViewModel = TextRefinementViewModel(refineTextUseCase, applicationScope)
    val translatorViewModel = TranslatorViewModel()
}
