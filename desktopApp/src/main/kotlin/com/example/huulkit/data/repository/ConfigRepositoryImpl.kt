package com.example.huulkit.data.repository

import com.example.huulkit.data.source.ConfigDataSource
import com.example.huulkit.domain.repository.ConfigRepository

/**
 * Implementation of the ConfigRepository interface
 */
class ConfigRepositoryImpl(private val configDataSource: ConfigDataSource) : ConfigRepository {

    /**
     * Get the Gemini API key
     * @return The Gemini API key as a string
     */
    override fun getGeminiApiKey(): String {
        return configDataSource.getGeminiApiKey()
    }

    /**
     * Update the Gemini API key
     * @param apiKey The new Gemini API key
     */
    override fun updateGeminiApiKey(apiKey: String) {
        configDataSource.updateGeminiApiKey(apiKey)
    }
}
