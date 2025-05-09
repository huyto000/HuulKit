package com.example.huulkit.domain.usecase

import com.example.huulkit.domain.repository.ConfigRepository

/**
 * Use case for getting the Gemini API key
 */
class GetGeminiApiKeyUseCase(private val configRepository: ConfigRepository) {
    
    /**
     * Executes the use case to get the Gemini API key
     * 
     * @return The Gemini API key as a string
     */
    operator fun invoke(): String {
        return configRepository.getGeminiApiKey()
    }
}