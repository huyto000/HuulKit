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
 * Service for text refinement using Google's Gemini model
 */
object GeminiService {
    
    /**
     * Checks if the Gemini API key is configured
     */
    fun isConfigured(): Boolean {
        val apiKey = ConfigManager.getGeminiApiKey()
        return apiKey.isNotBlank()
    }
    
    /**
     * Refines text based on the selected options using Gemini
     * 
     * @param text The original text to refine
     * @param options The refinement options
     * @return Result containing the refined text or an error
     */
    suspend fun refineText(text: String, options: RefinementOptions): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (!isConfigured()) {
                return@withContext Result.failure(Exception("Gemini API key not configured"))
            }
            
            val apiKey = ConfigManager.getGeminiApiKey()
            val model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .temperature(1.0)
                .build()
            
            val textRefiner = AiServices.builder(TextRefiner::class.java)
                .chatModel(model)
                .build()
            
            val refinedText = textRefiner.refineText(text, options.toPromptSuffix())
            Result.success(refinedText)
        } catch (e: Exception) {
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