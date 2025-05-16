package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.domain.usecase.GetGeminiApiKeyUseCase
import com.example.huulkit.domain.usecase.GetWeatherApiKeyUseCase
import com.example.huulkit.domain.usecase.UpdateGeminiApiKeyUseCase
import com.example.huulkit.domain.usecase.UpdateWeatherApiKeyUseCase

/**
 * ViewModel for configuration operations
 */
class ConfigViewModel(
    private val getGeminiApiKeyUseCase: GetGeminiApiKeyUseCase,
    private val updateGeminiApiKeyUseCase: UpdateGeminiApiKeyUseCase,
    private val getWeatherApiKeyUseCase: GetWeatherApiKeyUseCase,
    private val updateWeatherApiKeyUseCase: UpdateWeatherApiKeyUseCase
) {
    // UI state
    var geminiApiKeyInput by mutableStateOf("")
        private set

    var weatherApiKeyInput by mutableStateOf("")
        private set

    var showApiKeyDialog by mutableStateOf(false)
        private set

    /**
     * Initializes the view model by loading the current API keys
     */
    fun initialize() {
        geminiApiKeyInput = getGeminiApiKeyUseCase()
        weatherApiKeyInput = getWeatherApiKeyUseCase()
    }

    /**
     * Updates the Gemini API key input
     */
    fun updateGeminiApiKeyInput(apiKey: String) {
        geminiApiKeyInput = apiKey
    }

    /**
     * Updates the Weather API key input
     */
    fun updateWeatherApiKeyInput(apiKey: String) {
        weatherApiKeyInput = apiKey
    }

    /**
     * Shows the API key dialog
     */
    fun showDialog() {
        geminiApiKeyInput = getGeminiApiKeyUseCase()
        weatherApiKeyInput = getWeatherApiKeyUseCase()
        showApiKeyDialog = true
    }

    /**
     * Hides the API key dialog
     */
    fun hideDialog() {
        showApiKeyDialog = false
    }

    /**
     * Saves the API keys
     */
    fun saveApiKeys() {
        updateGeminiApiKeyUseCase(geminiApiKeyInput)
        updateWeatherApiKeyUseCase(weatherApiKeyInput)
        showApiKeyDialog = false
    }
}
