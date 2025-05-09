package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.ai.Language
import com.example.huulkit.ai.TranslationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for translator operations
 */
class TranslatorViewModel {
    // UI state
    var englishText by mutableStateOf("")
        private set

    var swedishText by mutableStateOf("")
        private set

    var vietnameseText by mutableStateOf("")
        private set

    // Loading states
    var isEnglishLoading by mutableStateOf(false)
        private set

    var isSwedishLoading by mutableStateOf(false)
        private set

    var isVietnameseLoading by mutableStateOf(false)
        private set

    // Focus tracking
    var focusedLanguage by mutableStateOf<Language?>(null)
//        private set

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    /**
     * Updates the English text
     */
    fun updateEnglishText(text: String) {
        englishText = text
    }

    /**
     * Updates the Swedish text
     */
    fun updateSwedishText(text: String) {
        swedishText = text
    }

    /**
     * Updates the Vietnamese text
     */
    fun updateVietnameseText(text: String) {
        vietnameseText = text
    }

    /**
     * Sets the focused language
     */
    fun updateFocusedLanguage(language: Language?) {
        focusedLanguage = language
    }

    /**
     * Gets the text for a specific language
     */
    private fun getTextForLanguage(language: Language): String {
        return when (language) {
            Language.ENGLISH -> englishText
            Language.SWEDISH -> swedishText
            Language.VIETNAMESE -> vietnameseText
        }
    }

    /**
     * Sets the loading state for a specific language
     */
    private fun setLoadingState(language: Language, isLoading: Boolean) {
        when (language) {
            Language.ENGLISH -> isEnglishLoading = isLoading
            Language.SWEDISH -> isSwedishLoading = isLoading
            Language.VIETNAMESE -> isVietnameseLoading = isLoading
        }
    }

    /**
     * Updates the text for a specific language
     */
    private fun updateTextForLanguage(language: Language, text: String) {
        when (language) {
            Language.ENGLISH -> englishText = text
            Language.SWEDISH -> swedishText = text
            Language.VIETNAMESE -> vietnameseText = text
        }
    }

    /**
     * Translates text from the focused language to all other languages
     */
    fun translate() {
        val sourceLanguage = focusedLanguage ?: return
        val sourceText = getTextForLanguage(sourceLanguage)

        if (sourceText.isBlank()) return

        // Get all target languages (all languages except the source)
        val targetLanguages = Language.values().filter { it != sourceLanguage }

        // Use AtomicInteger for thread-safe counting
        val remainingTranslations = java.util.concurrent.atomic.AtomicInteger(targetLanguages.size)

        // Translate to each target language
        targetLanguages.forEach { targetLanguage ->
            // Set loading state for target language
            setLoadingState(targetLanguage, true)

            coroutineScope.launch {
                try {
                    val result = TranslationService.translateText(
                        sourceText,
                        sourceLanguage,
                        targetLanguage
                    )

                    result.onSuccess { translatedText ->
                        updateTextForLanguage(targetLanguage, translatedText)
                    }.onFailure { error ->
                        // Handle error (could add error state to UI)
                        println("Translation error: ${error.message}")
                    }
                } finally {
                    // Clear loading state
                    setLoadingState(targetLanguage, false)

                    // Decrement remaining translations count
                    val remaining = remainingTranslations.decrementAndGet()

                    // If all translations are complete, clear focus
                    if (remaining == 0) {
                        updateFocusedLanguage(null)
                    }
                }
            }
        }
    }
}
