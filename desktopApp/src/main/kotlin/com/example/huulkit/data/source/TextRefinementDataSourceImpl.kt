package com.example.huulkit.data.source

import com.example.huulkit.ai.GeminiService
import com.example.huulkit.domain.model.RefinementOptions
import com.example.huulkit.ai.RefinementOptions as OldRefinementOptions

/**
 * Implementation of the TextRefinementDataSource interface using the GeminiService
 */
class TextRefinementDataSourceImpl : TextRefinementDataSource {
    
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
        
        return GeminiService.refineText(text, oldOptions)
    }
    
    /**
     * Checks if the text refinement service is configured and ready to use
     * 
     * @return True if the service is configured, false otherwise
     */
    override fun isServiceConfigured(): Boolean {
        return GeminiService.isConfigured()
    }
}