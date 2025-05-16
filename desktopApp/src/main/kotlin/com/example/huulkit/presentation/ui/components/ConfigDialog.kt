package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.huulkit.presentation.theme.HuulkitBlue
import com.example.huulkit.presentation.viewmodel.ConfigViewModel

/**
 * Dialog for configuring the Gemini API key
 */
@Composable
fun ConfigDialog(
    configViewModel: ConfigViewModel
) {
    if (configViewModel.showApiKeyDialog) {
        Dialog(onDismissRequest = { configViewModel.hideDialog() }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).width(IntrinsicSize.Min).widthIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Configure API Keys",
                        style = MaterialTheme.typography.h6
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = configViewModel.geminiApiKeyInput,
                        onValueChange = { configViewModel.updateGeminiApiKeyInput(it) },
                        label = { Text("Gemini API Key") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = configViewModel.weatherApiKeyInput,
                        onValueChange = { configViewModel.updateWeatherApiKeyInput(it) },
                        label = { Text("Weather API Key") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { configViewModel.hideDialog() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.surface
                            )
                        ) {
                            Text("Cancel")
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(
                            onClick = { configViewModel.saveApiKeys() },
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
