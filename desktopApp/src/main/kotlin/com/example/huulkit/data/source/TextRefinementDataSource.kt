package com.example.huulkit.data.source

import com.example.huulkit.domain.model.RefinementOptions

/**
 * Data source interface for text refinement operations
 */
interface TextRefinementDataSource {
    /**
     * Refines text based on the selected options
     * 
     * @param text The original text to refine
     * @param options The refinement options
     * @return Result containing the refined text or an error
     */
    suspend fun refineText(text: String, options: RefinementOptions): Result<String>
    
    /**
     * Checks if the text refinement service is configured and ready to use
     * 
     * @return True if the service is configured, false otherwise
     */
    fun isServiceConfigured(): Boolean
}