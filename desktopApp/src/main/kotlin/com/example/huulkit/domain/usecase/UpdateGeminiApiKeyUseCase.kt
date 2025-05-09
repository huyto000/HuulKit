package com.example.huulkit.domain.usecase

import com.example.huulkit.domain.repository.ConfigRepository

/**
 * Use case for updating the Gemini API key
 */
class UpdateGeminiApiKeyUseCase(private val configRepository: ConfigRepository) {
    
    /**
     * Executes the use case to update the Gemini API key
     * 
     * @param apiKey The new Gemini API key
     */
    operator fun invoke(apiKey: String) {
        configRepository.updateGeminiApiKey(apiKey)
    }
}