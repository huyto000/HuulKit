package com.example.huulkit.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for parsing weather API responses
 */

@Serializable
data class WeatherResponse(
    val location: Location,
    val current: Current
)

@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerialName("tz_id") val tzId: String,
    @SerialName("localtime_epoch") val localtimeEpoch: Long,
    val localtime: String
)

@Serializable
data class Current(

    @SerialName("last_updated") val lastUpdated: String,
    @SerialName("temp_c") val tempC: Double,
    val condition: Condition,
)

@Serializable
data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

/**
 * Enum for cities to fetch weather data
 */
enum class City(val displayName: String, val queryName: String) {
    LONDON("London", "London"),
    STOCKHOLM("Stockholm", "Stockholm"),
    HANOI("Hanoi", "Hanoi")
}

/**
 * Data class to hold weather information for UI display
 */
data class WeatherInfo(
    val temperature: String = "30°", // Default value
    val iconUrl: String = "", // Default empty
    val localTime: String = "" // Default empty
)