package com.example.huulkit.data.repository

import com.example.huulkit.data.source.TextRefinementDataSource
import com.example.huulkit.domain.model.RefinementOptions
import com.example.huulkit.domain.repository.TextRefinementRepository

/**
 * Implementation of the TextRefinementRepository interface
 */
class TextRefinementRepositoryImpl(private val textRefinementDataSource: TextRefinementDataSource) : TextRefinementRepository {
    
    /**
     * Refines text based on the selected options
     * 
     * @param text The original text to refine
     * @param options The refinement options
     * @return Result containing the refined text or an error
     */
    override suspend fun refineText(text: String, options: RefinementOptions): Result<String> {
        return textRefinementDataSource.refineText(text, options)
    }
    
    /**
     * Checks if the text refinement service is configured and ready to use
     * 
     * @return True if the service is configured, false otherwise
     */
    override fun isServiceConfigured(): Boolean {
        return textRefinementDataSource.isServiceConfigured()
    }
}