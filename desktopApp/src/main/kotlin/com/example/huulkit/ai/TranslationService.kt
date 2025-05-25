package com.example.huulkit.ai

import com.example.huulkit.config.ConfigManager
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import kotlin.getValue

/**
 * Enum representing supported languages for translation
 */
enum class Language(val displayName: String) {
    ENGLISH("English"),
    SWEDISH("Swedish"),
    VIETNAMESE("Vietnamese")
}

/**
 * Service for translating text between languages using Google's Gemini model
 */
class TranslationService : KoinComponent {
    private val model: GoogleAiGeminiChatModel by inject()
    private val logger = LoggerFactory.getLogger(TranslationService::class.java)

    /**
     * Checks if the Gemini API key is configured
     */
    fun isConfigured(): Boolean {
        logger.debug("Checking if Gemini API key is configured for translation")
        val apiKey = ConfigManager.getGeminiApiKey()
        val isConfigured = apiKey.isNotBlank()
        logger.debug("Gemini API key configured for translation: {}", isConfigured)
        return isConfigured
    }

    /**
     * Translates text from source language to target language using Gemini
     * 
     * @param text The text to translate
     * @param sourceLanguage The source language
     * @param targetLanguage The target language
     * @return Result containing the translated text or an error
     */
    suspend fun translateText(
        text: String, 
        sourceLanguage: Language, 
        targetLanguage: Language
    ): Result<String> = withContext(Dispatchers.IO) {
        logger.info("Translating text from {} to {}", sourceLanguage.displayName, targetLanguage.displayName)
        logger.debug("Text length: {}", text.length)

        try {
            if (!isConfigured()) {
                logger.error("Cannot translate text: Gemini API key not configured")
                return@withContext Result.failure(Exception("Gemini API key not configured"))
            }

            if (text.isBlank()) {
                logger.debug("Text is blank, returning empty result")
                return@withContext Result.success("")
            }

            logger.debug("Creating translator with Gemini model")
            val translator = AiServices.builder(TextTranslator::class.java)
                .chatModel(model)
                .build()

            logger.info("Calling Gemini API to translate text")
            val translatedText = translator.translateText(
                text, 
                sourceLanguage.displayName, 
                targetLanguage.displayName
            )
            logger.info("Translation successful")
            logger.debug("Translated text length: {}", translatedText.length)
            Result.success(translatedText)
        } catch (e: Exception) {
            logger.error("Error translating text with Gemini API: {}", e.message, e)
            Result.failure(Exception("Error with Gemini API: ${e.message}", e))
        }
    }

    /**
     * Interface for the AI service to translate text
     */
    private interface TextTranslator {
        @SystemMessage("You are a professional translator that accurately translates text between languages. " +
                "Your task is to translate the input text from the source language to the target language. " +
                "Maintain the original meaning, tone, and style as much as possible. " +
                "Only return the translated text, without quotes, explanation, or other commentary.")
        @UserMessage("""
            Translate the following text from {{sourceLanguage}} to {{targetLanguage}}:

            {{text}}

            Return only the translated text without any explanations, quotes or additional commentary.
        """)
        fun translateText(
            @V("text") text: String, 
            @V("sourceLanguage") sourceLanguage: String, 
            @V("targetLanguage") targetLanguage: String
        ): String
    }
}
