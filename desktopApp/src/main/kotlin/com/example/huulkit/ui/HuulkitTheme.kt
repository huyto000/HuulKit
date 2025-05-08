package com.example.huulkit.ui

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define the main color #384c77 as a Color object
val HuulkitBlue = Color(0xFF384C77)

// Define colors for light theme
private val LightColorPalette = lightColors(
    primary = HuulkitBlue,
    primaryVariant = HuulkitBlue.copy(alpha = 0.8f),
    secondary = Color(0xFF03DAC5),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Define colors for dark theme
private val DarkColorPalette = darkColors(
    primary = HuulkitBlue.copy(alpha = 0.9f),
    primaryVariant = HuulkitBlue.copy(alpha = 0.6f),
    secondary = Color(0xFF03DAC5),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

/**
 * Custom theme for Huulkit application using #384c77 as the primary color
 */
@Composable
fun HuulkitTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
} 