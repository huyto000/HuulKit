package com.example.huulkit.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Serializable
data class ApiConfig(
    val geminiApiKey: String = ""
)

/**
 * Manages application configuration including API keys for Gemini
 */
object ConfigManager {
    private const val CONFIG_DIR = ".huulkit"
    private const val CONFIG_FILE = "config.json"
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    
    private val configDir: File by lazy {
        val userHome = System.getProperty("user.home")
        val dir = File(userHome, CONFIG_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        dir
    }
    
    private val configFile: File by lazy {
        File(configDir, CONFIG_FILE)
    }
    
    // Load config from file or create default if not exists
    fun loadConfig(): ApiConfig {
        return if (configFile.exists()) {
            try {
                val content = Files.readString(Paths.get(configFile.toURI()))
                json.decodeFromString<ApiConfig>(content)
            } catch (e: Exception) {
                println("Error loading config: ${e.message}")
                ApiConfig() // Return default if error
            }
        } else {
            ApiConfig() // Return default if file doesn't exist
        }
    }
    
    // Save config to file
    fun saveConfig(config: ApiConfig) {
        try {
            val content = json.encodeToString(config)
            Files.writeString(Paths.get(configFile.toURI()), content)
        } catch (e: Exception) {
            println("Error saving config: ${e.message}")
        }
    }
    
    // Get Gemini API key
    fun getGeminiApiKey(): String {
        return loadConfig().geminiApiKey
    }
    
    // Update Gemini API key
    fun updateGeminiApiKey(apiKey: String) {
        val config = loadConfig()
        val updatedConfig = config.copy(geminiApiKey = apiKey)
        saveConfig(updatedConfig)
    }
} 