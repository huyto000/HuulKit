package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.domain.model.RefinementOptions
import com.example.huulkit.domain.usecase.RefineTextUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for text refinement operations
 */
class TextRefinementViewModel(
    private val refineTextUseCase: RefineTextUseCase,
    private val coroutineScope: CoroutineScope
) {
    // UI state
    var inputText by mutableStateOf("")
        private set
    
    var outputText by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorState by mutableStateOf<String?>(null)
        private set
    
    // Refinement options
    var shorten by mutableStateOf(true)
        private set
    
    var clarify by mutableStateOf(false)
        private set
    
    var makeKinder by mutableStateOf(false)
        private set
    
    var polish by mutableStateOf(false)
        private set
    
    var combineAll by mutableStateOf(false)
        private set
    
    /**
     * Updates the input text
     */
    fun updateInputText(text: String) {
        inputText = text
    }
    
    /**
     * Updates the shorten option
     */
    fun updateShorten(value: Boolean) {
        shorten = value
    }
    
    /**
     * Updates the clarify option
     */
    fun updateClarify(value: Boolean) {
        clarify = value
    }
    
    /**
     * Updates the make kinder option
     */
    fun updateMakeKinder(value: Boolean) {
        makeKinder = value
    }
    
    /**
     * Updates the polish option
     */
    fun updatePolish(value: Boolean) {
        polish = value
    }
    
    /**
     * Updates the combine all option
     */
    fun updateCombineAll(value: Boolean) {
        combineAll = value
    }
    
    /**
     * Refines the input text based on the selected options
     */
    fun refineText() {
        // Clear previous error
        errorState = null
        
        if (inputText.isBlank()) {
            errorState = "Input text cannot be empty"
            return
        }
        
        isLoading = true
        
        // Create refinement options
        val options = RefinementOptions(
            shorten = shorten,
            clarify = clarify,
            makeKinder = makeKinder,
            polish = polish,
            combineAll = combineAll
        )
        
        // Launch coroutine to call the use case
        coroutineScope.launch {
            try {
                val result = refineTextUseCase(inputText, options)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { refined ->
                            outputText = refined
                            errorState = null
                        },
                        onFailure = { error ->
                            errorState = "Error: ${error.message}"
                        }
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorState = "Error: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }
}