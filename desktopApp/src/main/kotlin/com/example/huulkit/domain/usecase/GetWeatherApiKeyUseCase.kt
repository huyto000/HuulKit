package com.example.huulkit.domain.usecase

import com.example.huulkit.domain.repository.ConfigRepository

/**
 * Use case for getting the Weather API key
 */
class GetWeatherApiKeyUseCase(private val configRepository: ConfigRepository) {
    
    /**
     * Executes the use case to get the Weather API key
     * 
     * @return The Weather API key as a string
     */
    operator fun invoke(): String {
        return configRepository.getWeatherApiKey()
    }
}