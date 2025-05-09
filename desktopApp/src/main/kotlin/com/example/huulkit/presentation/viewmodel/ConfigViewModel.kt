package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.domain.usecase.GetGeminiApiKeyUseCase
import com.example.huulkit.domain.usecase.UpdateGeminiApiKeyUseCase

/**
 * ViewModel for configuration operations
 */
class ConfigViewModel(
    private val getGeminiApiKeyUseCase: GetGeminiApiKeyUseCase,
    private val updateGeminiApiKeyUseCase: UpdateGeminiApiKeyUseCase
) {
    // UI state
    var apiKeyInput by mutableStateOf("")
        private set
    
    var showApiKeyDialog by mutableStateOf(false)
        private set
    
    /**
     * Initializes the view model by loading the current API key
     */
    fun initialize() {
        apiKeyInput = getGeminiApiKeyUseCase()
    }
    
    /**
     * Updates the API key input
     */
    fun updateApiKeyInput(apiKey: String) {
        apiKeyInput = apiKey
    }
    
    /**
     * Shows the API key dialog
     */
    fun showDialog() {
        apiKeyInput = getGeminiApiKeyUseCase()
        showApiKeyDialog = true
    }
    
    /**
     * Hides the API key dialog
     */
    fun hideDialog() {
        showApiKeyDialog = false
    }
    
    /**
     * Saves the API key
     */
    fun saveApiKey() {
        updateGeminiApiKeyUseCase(apiKeyInput)
        showApiKeyDialog = false
    }
}