package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.ai.Language
import com.example.huulkit.ai.TranslationService
import com.example.huulkit.weather.City
import com.example.huulkit.weather.WeatherInfo
import com.example.huulkit.weather.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for translator operations
 */
class TranslatorViewModel(
    private val weatherService: WeatherService
) {
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

    // Weather data
    var londonWeather by mutableStateOf(WeatherInfo())
        private set

    var stockholmWeather by mutableStateOf(WeatherInfo())
        private set

    var hanoiWeather by mutableStateOf(WeatherInfo())
        private set

    // Weather loading state
    var isWeatherLoading by mutableStateOf(false)
        private set

    // Error state for weather API key
    var weatherApiKeyError by mutableStateOf(false)
        private set

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Constants for weather update interval
    private val weatherUpdateIntervalMs = 30 * 60 * 1000L // 30 minutes in milliseconds

    init {
        // Initial fetch of weather data
        fetchWeatherData()

        // Start periodic weather updates
        startPeriodicWeatherUpdates()
    }

    /**
     * Starts periodic weather updates every 30 minutes
     */
    private fun startPeriodicWeatherUpdates() {
        coroutineScope.launch {
            while (true) {
                delay(weatherUpdateIntervalMs)
                fetchWeatherData()
            }
        }
    }

    /**
     * Fetches weather data for all cities
     */
    fun fetchWeatherData() {
        isWeatherLoading = true
        // Reset the API key error state
        weatherApiKeyError = false

        coroutineScope.launch {
            try {
                // Fetch weather for London
                weatherService.getWeatherForCity(City.LONDON).onSuccess { weather ->
                    londonWeather = weather
                }.onFailure { error ->
                    if (error is IllegalStateException && 
                        (error.message?.contains("API key is not set") == true || 
                         error.message?.contains("You must provide correct API key") == true)) {
                        weatherApiKeyError = true
                    }
                }

                // Only continue if we don't have an API key error
                if (!weatherApiKeyError) {
                    // Fetch weather for Stockholm
                    weatherService.getWeatherForCity(City.STOCKHOLM).onSuccess { weather ->
                        stockholmWeather = weather
                    }

                    // Fetch weather for Hanoi
                    weatherService.getWeatherForCity(City.HANOI).onSuccess { weather ->
                        hanoiWeather = weather
                    }
                }
            } catch (e: Exception) {
                // Handle error
                println("Error fetching weather data: ${e.message}")
            } finally {
                isWeatherLoading = false
            }
        }
    }

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
