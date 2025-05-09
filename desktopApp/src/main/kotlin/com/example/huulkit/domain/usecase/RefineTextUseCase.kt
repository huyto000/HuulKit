package com.example.huulkit.domain.usecase

import com.example.huulkit.domain.model.RefinementOptions
import com.example.huulkit.domain.repository.TextRefinementRepository

/**
 * Use case for refining text
 */
class RefineTextUseCase(private val textRefinementRepository: TextRefinementRepository) {
    
    /**
     * Executes the use case to refine text
     * 
     * @param text The original text to refine
     * @param options The refinement options
     * @return Result containing the refined text or an error
     */
    suspend operator fun invoke(text: String, options: RefinementOptions): Result<String> {
        if (text.isBlank()) {
            return Result.failure(IllegalArgumentException("Text cannot be empty"))
        }
        
        if (!textRefinementRepository.isServiceConfigured()) {
            return Result.failure(IllegalStateException("Text refinement service is not configured"))
        }
        
        return textRefinementRepository.refineText(text, options)
    }
}