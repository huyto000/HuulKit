package com.example.huulkit.domain.usecase

import com.example.huulkit.domain.repository.ConfigRepository

/**
 * Use case for updating the Weather API key
 */
class UpdateWeatherApiKeyUseCase(private val configRepository: ConfigRepository) {
    
    /**
     * Executes the use case to update the Weather API key
     * 
     * @param apiKey The new Weather API key
     */
    operator fun invoke(apiKey: String) {
        configRepository.updateWeatherApiKey(apiKey)
    }
}