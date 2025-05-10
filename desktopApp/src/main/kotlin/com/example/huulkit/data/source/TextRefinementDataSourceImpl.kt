package com.example.huulkit.data.source

import com.example.huulkit.ai.GeminiService
import com.example.huulkit.domain.model.RefinementOptions
import com.example.huulkit.ai.RefinementOptions as OldRefinementOptions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Implementation of the TextRefinementDataSource interface using the GeminiService
 */
class TextRefinementDataSourceImpl : TextRefinementDataSource, KoinComponent {
    private val geminiService: GeminiService by inject()
    
    /**
     * Refines text based on the selected options
     * 
     * @param text The original text to refine
     * @param options The refinement options
     * @return Result containing the refined text or an error
     */
    override suspend fun refineText(text: String, options: RefinementOptions): Result<String> {
        // Convert domain model to data model
        val oldOptions = OldRefinementOptions(
            shorten = options.shorten,
            clarify = options.clarify,
            makeKinder = options.makeKinder,
            polish = options.polish,
            combineAll = options.combineAll
        )
        
        return geminiService.refineText(text, oldOptions)
    }
    
    /**
     * Checks if the text refinement service is configured and ready to use
     * 
     * @return True if the service is configured, false otherwise
     */
    override fun isServiceConfigured(): Boolean {
        return geminiService.isConfigured()
    }
}