package com.example.huulkit.ai

import com.example.huulkit.config.ConfigManager
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
object TranslationService {
    
    /**
     * Checks if the Gemini API key is configured
     */
    fun isConfigured(): Boolean {
        val apiKey = ConfigManager.getGeminiApiKey()
        return apiKey.isNotBlank()
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
        try {
            if (!isConfigured()) {
                return@withContext Result.failure(Exception("Gemini API key not configured"))
            }
            
            if (text.isBlank()) {
                return@withContext Result.success("")
            }
            
            val apiKey = ConfigManager.getGeminiApiKey()
            val model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .temperature(1.0) // Lower temperature for more accurate translations
                .build()
            
            val translator = AiServices.builder(TextTranslator::class.java)
                .chatModel(model)
                .build()
            
            val translatedText = translator.translateText(
                text, 
                sourceLanguage.displayName, 
                targetLanguage.displayName
            )
            Result.success(translatedText)
        } catch (e: Exception) {
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