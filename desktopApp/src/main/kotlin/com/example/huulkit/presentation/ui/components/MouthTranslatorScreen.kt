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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
            Text(
                "Mouth Translator", 
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )

            // English text area
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    // English flag icon
                    Image(
                        painter = painterResource("english.png"),
                        contentDescription = "English Flag",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "English",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )

                    // Loading indicator
                    if (viewModel.isEnglishLoading) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }

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

            // Swedish text area
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    // Swedish flag icon
                    Image(
                        painter = painterResource("sweden.png"),
                        contentDescription = "Swedish Flag",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Swedish",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )

                    // Loading indicator
                    if (viewModel.isSwedishLoading) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }

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

            // Vietnamese text area
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    // Vietnamese flag icon
                    Image(
                        painter = painterResource("vietnam.png"),
                        contentDescription = "Vietnamese Flag",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Vietnamese",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )

                    // Loading indicator
                    if (viewModel.isVietnameseLoading) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }

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

            // Translate button
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
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