package com.example.huulkit.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define the primary blue color used throughout the app
val HuulkitBlue = Color(0xFF0069C0)

// Light theme colors
private val LightColors = lightColors(
    primary = HuulkitBlue,
    primaryVariant = Color(0xFF0069C0),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Dark theme colors
private val DarkColors = darkColors(
    primary = HuulkitBlue,
    primaryVariant = Color(0xFF0069C0),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

/**
 * Huulkit theme composable that applies the appropriate color scheme based on dark mode setting
 */
@Composable
fun HuulkitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}