package com.example.huulkit.data.source

import com.example.huulkit.config.ConfigManager

/**
 * Implementation of the ConfigDataSource interface using the ConfigManager
 */
class ConfigDataSourceImpl : ConfigDataSource {

    /**
     * Get the Gemini API key
     * @return The Gemini API key as a string
     */
    override fun getGeminiApiKey(): String {
        return ConfigManager.getGeminiApiKey()
    }

    /**
     * Update the Gemini API key
     * @param apiKey The new Gemini API key
     */
    override fun updateGeminiApiKey(apiKey: String) {
        ConfigManager.updateGeminiApiKey(apiKey)
    }

    /**
     * Get the Weather API key
     * @return The Weather API key as a string
     */
    override fun getWeatherApiKey(): String {
        return ConfigManager.getWeatherApiKey()
    }

    /**
     * Update the Weather API key
     * @param apiKey The new Weather API key
     */
    override fun updateWeatherApiKey(apiKey: String) {
        ConfigManager.updateWeatherApiKey(apiKey)
    }
}
