package com.example.huulkit.weather

import com.example.huulkit.domain.repository.ConfigRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

/**
 * Service for fetching weather data from the Weather API
 */
class WeatherService(
    private val configRepository: ConfigRepository
) {
    private val logger = LoggerFactory.getLogger(WeatherService::class.java)
    private val baseUrl = "http://api.weatherapi.com/v1/current.json"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Fetches weather data for a specific city
     * @param city The city to fetch weather data for
     * @return Result containing WeatherInfo on success or an exception on failure
     */
    suspend fun getWeatherForCity(city: City): Result<WeatherInfo> = withContext(Dispatchers.IO) {
        try {
            logger.info("Fetching weather data for city: {}", city.displayName)
            val apiKey = configRepository.getWeatherApiKey()

            if (apiKey.isBlank()) {
                logger.error("Weather API key is not set")
                return@withContext Result.failure(IllegalStateException("Weather API key is not set"))
            }

            // Get the HttpResponse first to check the status
            val httpResponse = client.get(baseUrl) {
                parameter("key", apiKey)
                parameter("q", city.queryName)
                parameter("aqi", "no")
            }

            // Check if the status is not OK (200)
            if (httpResponse.status != HttpStatusCode.OK) {
                logger.error("Weather API returned error status: {}", httpResponse.status)
                return@withContext Result.failure(IllegalStateException("You must provide correct API key"))
            }

            // If status is OK, parse the response body
            val response: WeatherResponse = httpResponse.body()

            logger.debug("Weather data fetched successfully for city: {}", city.displayName)

            // Create WeatherInfo from response
            // Extract only the time part from the localtime string
            val localTimeString = response.location.localtime
            val timeOnly = if (localTimeString.contains(" ")) {
                // If the string contains a space, assume it's in "yyyy-MM-dd HH:mm" format
                // and extract the part after the space
                localTimeString.split(" ")[1]
            } else {
                // Fallback to the original string if it doesn't match the expected format
                localTimeString
            }

            val weatherInfo = WeatherInfo(
                temperature = "${response.current.tempC.toInt()}°",
                iconUrl = "https:${response.current.condition.icon}",
                localTime = timeOnly
            )

            Result.success(weatherInfo)
        } catch (e: Exception) {
            logger.error("Error fetching weather data for city {}: {}", city.displayName, e.message, e)
            Result.failure(e)
        }
    }

    /**
     * Closes the HTTP client
     */
    fun close() {
        client.close()
    }
}
