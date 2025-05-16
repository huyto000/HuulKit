package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image as SkiaImage
import java.io.ByteArrayInputStream
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.huulkit.ai.Language
import com.example.huulkit.presentation.theme.HuulkitBlue
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Loads an image from a URL and returns a Painter and loading state
 */
@Composable
fun rememberImagePainter(url: String): Pair<Painter, Boolean> {
    var painter by remember { mutableStateOf<Painter?>(null) }
    var isLoading by remember { mutableStateOf(url.isNotEmpty()) }

    LaunchedEffect(url) {
        if (url.isNotEmpty()) {
            isLoading = true
            try {
                val client = HttpClient(CIO)
                val response = client.get(url)
                val bytes = response.readBytes()
                client.close()

                val bitmap = withContext(Dispatchers.Default) {
                    val inputStream = ByteArrayInputStream(bytes)
                    val skiaImage = SkiaImage.makeFromEncoded(bytes)
                    skiaImage.toComposeImageBitmap()
                }

                painter = BitmapPainter(bitmap)
            } catch (e: Exception) {
                println("Error loading image: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Return the painter (or default) and loading state
    return Pair(
        painter ?: painterResource("icon.png"), // Fallback to a default icon
        isLoading
    )
}

/**
 * Screen for the Mouth Translator tab
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MouthTranslatorScreen(
    viewModel: TranslatorViewModel,
    modifier: Modifier = Modifier
) {
    val translatorScrollState = rememberScrollState()

    // Focus requesters for each text field
    val englishFocusRequester = remember { FocusRequester() }
    val swedishFocusRequester = remember { FocusRequester() }
    val vietnameseFocusRequester = remember { FocusRequester() }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(translatorScrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Mouth Translator", 
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )

                // Refresh weather button
                IconButton(
                    onClick = { viewModel.fetchWeatherData() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh weather info incase we want to manually do it",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            // English text area
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Flag, language name, and weather info wrapped in a container
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ) {
                        // Language name above flag
                        Text(
                            "English",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // English flag icon
                        Image(
                            painter = painterResource("english.png"),
                            contentDescription = "English Flag",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Weather and time info inline
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Temperature
                            Text(
                                viewModel.londonWeather.temperature,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            // Weather icon
                            if (viewModel.londonWeather.iconUrl.isNotEmpty()) {
                                val (painter, isLoading) = rememberImagePainter(viewModel.londonWeather.iconUrl)
                                if (isLoading) {
                                    // Show loading indicator while image is loading
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Weather Icon",
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            } else {
                                // Fallback to cloud icon if no weather icon URL
                                Text(
                                    "☁",
                                    style = MaterialTheme.typography.h6
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Local time for London
                            Text(
                                viewModel.londonLocalTime,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Loading indicator
                        if (viewModel.isEnglishLoading) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                // Right side: Input field and copy button
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = viewModel.englishText,
                            onValueChange = { viewModel.updateEnglishText(it) },
                            label = { Text("Enter English text") },
                            enabled = !viewModel.isEnglishLoading,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 100.dp, max = 150.dp)
                                .focusRequester(englishFocusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        viewModel.updateFocusedLanguage(Language.ENGLISH)
                                    }
                                }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { copyToClipboard(viewModel.englishText) },
                            enabled = viewModel.englishText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = HuulkitBlue.copy(alpha = 0.8f)
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Copy")
                        }
                    }
                }
            }

            // Swedish text area
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Flag, language name, and weather info wrapped in a container
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ) {
                        // Language name above flag
                        Text(
                            "Swedish",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Swedish flag icon
                        Image(
                            painter = painterResource("sweden.png"),
                            contentDescription = "Swedish Flag",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Weather and time info inline
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Temperature
                            Text(
                                viewModel.stockholmWeather.temperature,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            // Weather icon
                            if (viewModel.stockholmWeather.iconUrl.isNotEmpty()) {
                                val (painter, isLoading) = rememberImagePainter(viewModel.stockholmWeather.iconUrl)
                                if (isLoading) {
                                    // Show loading indicator while image is loading
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Weather Icon",
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            } else {
                                // Fallback to cloud icon if no weather icon URL
                                Text(
                                    "☁",
                                    style = MaterialTheme.typography.h6
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Local time for Stockholm
                            Text(
                                viewModel.stockholmLocalTime,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Loading indicator
                        if (viewModel.isSwedishLoading) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                // Right side: Input field and copy button
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = viewModel.swedishText,
                            onValueChange = { viewModel.updateSwedishText(it) },
                            label = { Text("Enter Swedish text") },
                            enabled = !viewModel.isSwedishLoading,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 100.dp, max = 150.dp)
                                .focusRequester(swedishFocusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        viewModel.updateFocusedLanguage(Language.SWEDISH)
                                    }
                                }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { copyToClipboard(viewModel.swedishText) },
                            enabled = viewModel.swedishText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = HuulkitBlue.copy(alpha = 0.8f)
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Copy")
                        }
                    }
                }
            }

            // Vietnamese text area
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Flag, language name, and weather info wrapped in a container
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ) {
                        // Language name above flag
                        Text(
                            "Vietnamese",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Vietnamese flag icon
                        Image(
                            painter = painterResource("vietnam.png"),
                            contentDescription = "Vietnamese Flag",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Weather and time info inline
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Temperature
                            Text(
                                viewModel.hanoiWeather.temperature,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            // Weather icon
                            if (viewModel.hanoiWeather.iconUrl.isNotEmpty()) {
                                val (painter, isLoading) = rememberImagePainter(viewModel.hanoiWeather.iconUrl)
                                if (isLoading) {
                                    // Show loading indicator while image is loading
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Weather Icon",
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            } else {
                                // Fallback to cloud icon if no weather icon URL
                                Text(
                                    "☁",
                                    style = MaterialTheme.typography.h6
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Local time for Hanoi
                            Text(
                                viewModel.hanoiLocalTime,
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Loading indicator
                        if (viewModel.isVietnameseLoading) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                // Right side: Input field and copy button
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = viewModel.vietnameseText,
                            onValueChange = { viewModel.updateVietnameseText(it) },
                            label = { Text("Enter Vietnamese text") },
                            enabled = !viewModel.isVietnameseLoading,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 100.dp, max = 150.dp)
                                .focusRequester(vietnameseFocusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        viewModel.updateFocusedLanguage(Language.VIETNAMESE)
                                    }
                                }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { copyToClipboard(viewModel.vietnameseText) },
                            enabled = viewModel.vietnameseText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = HuulkitBlue.copy(alpha = 0.8f)
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Copy")
                        }
                    }
                }
            }

            // Weather API key error message
            if (viewModel.weatherApiKeyError) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        backgroundColor = Color(0xFFFFEBEE), // Light red background
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text(
                            "Weather API key is not set or incorrect. Please provide a valid API key in the configuration dialog.",
                            color = Color.Red,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // Weather loading indicator
            if (viewModel.isWeatherLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Updating weather data...")
                }
            }

            // Translate button
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.translate() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = HuulkitBlue
                    ),
                    shape = MaterialTheme.shapes.medium,
                    enabled = viewModel.focusedLanguage != null
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Translate",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Translate")
                }
            }

            // Add some bottom padding for scrolling
            Spacer(Modifier.height(16.dp))
        }

        // Add scrollbar for better navigation
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(translatorScrollState)
        )
    }
}

/**
 * Copies the given text to the system clipboard
 */
private fun copyToClipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, null)
}
