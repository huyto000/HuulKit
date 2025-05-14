package com.example.huulkit.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(ConfigManager::class.java)

    private val configDir: File by lazy {
        val userHome = System.getProperty("user.home")
        val dir = File(userHome, CONFIG_DIR)
        if (!dir.exists()) {
            logger.info("Creating config directory: {}", dir.absolutePath)
            dir.mkdirs()
        }
        dir
    }

    private val configFile: File by lazy {
        File(configDir, CONFIG_FILE)
    }

    // Load config from file or create default if not exists
    fun loadConfig(): ApiConfig {
        logger.debug("Loading configuration from: {}", configFile.absolutePath)
        return if (configFile.exists()) {
            try {
                logger.debug("Config file exists, reading content")
                val content = Files.readString(Paths.get(configFile.toURI()))
                val config = json.decodeFromString<ApiConfig>(content)
                logger.debug("Configuration loaded successfully")
                config
            } catch (e: Exception) {
                logger.error("Error loading config: {}", e.message, e)
                logger.info("Using default configuration due to error")
                ApiConfig() // Return default if error
            }
        } else {
            logger.info("Config file does not exist, using default configuration")
            ApiConfig() // Return default if file doesn't exist
        }
    }

    // Save config to file
    fun saveConfig(config: ApiConfig) {
        logger.debug("Saving configuration to: {}", configFile.absolutePath)
        try {
            val content = json.encodeToString(config)
            logger.debug("Writing configuration to file")
            Files.writeString(Paths.get(configFile.toURI()), content)
            logger.debug("Configuration saved successfully")
        } catch (e: Exception) {
            logger.error("Error saving config: {}", e.message, e)
        }
    }

    // Get Gemini API key
    fun getGeminiApiKey(): String {
        logger.debug("Getting Gemini API key")
        val apiKey = loadConfig().geminiApiKey
        logger.debug("Gemini API key retrieved, length: {}", apiKey.length)
        return apiKey
    }

    // Update Gemini API key
    fun updateGeminiApiKey(apiKey: String) {
        logger.info("Updating Gemini API key")
        val config = loadConfig()
        val updatedConfig = config.copy(geminiApiKey = apiKey)
        logger.debug("Saving updated configuration with new Gemini API key")
        saveConfig(updatedConfig)
        logger.info("Gemini API key updated successfully")
    }
} 
