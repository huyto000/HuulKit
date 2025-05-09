package com.example.huulkit.domain.repository

/**
 * Repository interface for managing application configuration
 */
interface ConfigRepository {
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