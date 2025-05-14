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

/**
 * Service for text refinement using Google's Gemini model
 */
class GeminiService : KoinComponent {
    private val model: GoogleAiGeminiChatModel by inject()
    private val logger = LoggerFactory.getLogger(GeminiService::class.java)

    /**
     * Checks if the Gemini API key is configured
     */
    fun isConfigured(): Boolean {
        logger.debug("Checking if Gemini API key is configured")
        val apiKey = ConfigManager.getGeminiApiKey()
        val isConfigured = apiKey.isNotBlank()
        logger.debug("Gemini API key configured: {}", isConfigured)
        return isConfigured
    }

    /**
     * Refines text based on the selected options using Gemini
     * 
     * @param text The original text to refine
     * @param options The refinement options
     * @return Result containing the refined text or an error
     */
    suspend fun refineText(text: String, options: RefinementOptions): Result<String> = withContext(Dispatchers.IO) {
        logger.info("Refining text with options: {}", options)
        try {
            if (!isConfigured()) {
                logger.error("Cannot refine text: Gemini API key not configured")
                return@withContext Result.failure(Exception("Gemini API key not configured"))
            }

            logger.debug("Creating text refiner with Gemini model")
            val textRefiner = AiServices.builder(TextRefiner::class.java)
                .chatModel(model)
                .build()

            logger.info("Calling Gemini API to refine text")
            val refinedText = textRefiner.refineText(text, options.toPromptSuffix())
            logger.info("Text refinement successful")
            logger.debug("Refined text length: {}", refinedText.length)
            Result.success(refinedText)
        } catch (e: Exception) {
            logger.error("Error refining text with Gemini API: {}", e.message, e)
            Result.failure(Exception("Error with Gemini API: ${e.message}", e))
        }
    }

    /**
     * Interface for the AI service to refine text
     */
    private interface TextRefiner {
        @SystemMessage("You are a helpful assistant that refines text to make it better. " +
                "Your task is to take the input text and refine it according to the instructions. " +
                "Only return the refined text, without quotes, explanation, or other commentary.")
        @UserMessage("""
            Original text: {{text}}

            {{instructions}}

            Return only the refined text without any explanations, quotes or additional commentary.
        """)
        fun refineText(@V("text") text: String, @V("instructions") instructions: String): String
    }
} 
