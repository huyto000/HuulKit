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
}