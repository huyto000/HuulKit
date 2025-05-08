package com.example.huulkit.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.huulkit.ai.GeminiService
import com.example.huulkit.ai.RefinementOptions
import com.example.huulkit.config.ConfigManager
import kotlinx.coroutines.launch
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun HuulkitApp() {
    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorState by remember { mutableStateOf<String?>(null) }

    // Dialog state for API key configuration
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var apiKeyInput by remember { mutableStateOf("") }

    // Refinement options state - set shorten to true by default
    var shorten by remember { mutableStateOf(true) }
    var clarify by remember { mutableStateOf(false) }
    var makeKinder by remember { mutableStateOf(false) }
    var polish by remember { mutableStateOf(false) }
    var combineAll by remember { mutableStateOf(false) }
    
    // Scope for launching coroutines
    val scope = rememberCoroutineScope()
    
    // Scrollable state for responsive content
    val scrollState = rememberScrollState()

    HuulkitTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Huulkit: Make your mouth better") },
                    actions = {
                        IconButton(onClick = {
                            // Show API key dialog
                            apiKeyInput = ConfigManager.getGeminiApiKey()
                            showApiKeyDialog = true
                        }) {
                            Icon(Icons.Filled.Settings, "Configure Gemini API Key")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Main scrollable content
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Enter your sentence below:", 
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("Original Sentence") },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 150.dp)
                    )
                    
                    Text(
                        "Refinement Options:", 
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    // Refinement options in a responsive grid using standard Row and Column layout
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RefineOption(checked = shorten, onCheckedChange = { shorten = it }, text = "Shorten", 
                                modifier = Modifier.weight(1f, fill = false))
                            RefineOption(checked = clarify, onCheckedChange = { clarify = it }, text = "Clarify",
                                modifier = Modifier.weight(1f, fill = false))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RefineOption(checked = makeKinder, onCheckedChange = { makeKinder = it }, text = "Make Kinder",
                                modifier = Modifier.weight(1f, fill = false))
                            RefineOption(checked = polish, onCheckedChange = { polish = it }, text = "Polish",
                                modifier = Modifier.weight(1f, fill = false))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RefineOption(checked = combineAll, onCheckedChange = { combineAll = it }, text = "Combine All")
                        }
                    }
                    
                    // Error message
                    errorState?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }
                    
                    // Refine button
                    Button(
                        onClick = { 
                            // Clear previous error
                            errorState = null
                            
                            // Check if API key is configured
                            if (!GeminiService.isConfigured()) {
                                errorState = "Gemini API key not configured. Click the settings icon to configure."
                                return@Button
                            }
                            
                            isLoading = true
                            
                            // Create refinement options object
                            val options = RefinementOptions(
                                shorten = shorten,
                                clarify = clarify,
                                makeKinder = makeKinder,
                                polish = polish,
                                combineAll = combineAll
                            )
                            
                            // Launch coroutine to call the Gemini service
                            scope.launch {
                                try {
                                    val result = GeminiService.refineText(inputText, options)
                                    result.fold(
                                        onSuccess = { refined ->
                                            outputText = refined
                                            errorState = null
                                        },
                                        onFailure = { error ->
                                            errorState = "Error: ${error.message}"
                                        }
                                    )
                                } catch (e: Exception) {
                                    errorState = "Error: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        enabled = !isLoading && inputText.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = HuulkitBlue
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp), 
                                strokeWidth = 2.dp, 
                                color = LocalContentColor.current
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Processing...")
                        } else {
                            Text("Refine Sentence")
                        }
                    }
                    
                    Text(
                        "Refined Sentence:", 
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = outputText,
                        onValueChange = { /* Read-only */ },
                        label = { Text("Output") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 150.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { 
                                // Copy to clipboard
                                copyToClipboard(outputText)
                            },
                            enabled = outputText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = HuulkitBlue.copy(alpha = 0.8f)
                            )
                        ) {
                            Text("Copy Output")
                        }
                    }
                    
                    // Add some bottom padding for scrolling
                    Spacer(Modifier.height(16.dp))
                }
                
                // Add scrollbar for better navigation
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }
        }
        
        // API Key Configuration Dialog
        if (showApiKeyDialog) {
            Dialog(onDismissRequest = { showApiKeyDialog = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).width(IntrinsicSize.Min).widthIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Configure Gemini API Key",
                            style = MaterialTheme.typography.h6
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = apiKeyInput,
                            onValueChange = { apiKeyInput = it },
                            label = { Text("Gemini API Key") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { showApiKeyDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                )
                            ) {
                                Text("Cancel")
                            }
                            
                            Spacer(Modifier.width(8.dp))
                            
                            Button(
                                onClick = {
                                    // Save API key to configuration
                                    ConfigManager.updateGeminiApiKey(apiKeyInput)
                                    showApiKeyDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = HuulkitBlue
                                )
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable composable for refinement options
 */
@Composable
private fun RefineOption(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier.padding(4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = HuulkitBlue
            )
        )
        Text(text)
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