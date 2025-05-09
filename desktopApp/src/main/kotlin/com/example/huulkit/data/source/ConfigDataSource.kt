package com.example.huulkit.data.source

/**
 * Data source interface for configuration data
 */
interface ConfigDataSource {
    /**
     * Get the Gemini API key
     * @return The Gemini API key as a string
     */
    fun getGeminiApiKey(): String
    
    /**
     * Update the Gemini API key
     * @param apiKey The new Gemini API key
     */
    fun updateGeminiApiKey(apiKey: String)
}