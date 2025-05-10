package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.huulkit.presentation.theme.HuulkitBlue
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Screen for the Mouth Helper tab
 */
@Composable
fun MouthHelperScreen(
    viewModel: TextRefinementViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Enter your sentence below:", 
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        
        OutlinedTextField(
            value = viewModel.inputText,
            onValueChange = { viewModel.updateInputText(it) },
            label = { Text("Original Sentence") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp, max = 100.dp)
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
                RefineOption(
                    checked = viewModel.shorten, 
                    onCheckedChange = { viewModel.updateShorten(it) }, 
                    text = "Shorten", 
                    modifier = Modifier.weight(1f, fill = false)
                )
                RefineOption(
                    checked = viewModel.clarify, 
                    onCheckedChange = { viewModel.updateClarify(it) }, 
                    text = "Clarify",
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RefineOption(
                    checked = viewModel.makeKinder, 
                    onCheckedChange = { viewModel.updateMakeKinder(it) }, 
                    text = "Make Kinder",
                    modifier = Modifier.weight(1f, fill = false)
                )
                RefineOption(
                    checked = viewModel.polish, 
                    onCheckedChange = { viewModel.updatePolish(it) }, 
                    text = "Polish",
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                RefineOption(
                    checked = viewModel.combineAll, 
                    onCheckedChange = { viewModel.updateCombineAll(it) }, 
                    text = "Combine All"
                )
            }
        }
        
        // Error message
        viewModel.errorState?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }
        
        // Refine button
        Button(
            onClick = { viewModel.refineText() },
            enabled = !viewModel.isLoading && viewModel.inputText.isNotBlank(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = HuulkitBlue
            )
        ) {
            if (viewModel.isLoading) {
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
            value = viewModel.outputText,
            onValueChange = { /* Read-only */ },
            label = { Text("Output") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp, max = 100.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { copyToClipboard(viewModel.outputText) },
                enabled = viewModel.outputText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = HuulkitBlue.copy(alpha = 0.8f)
                )
            ) {
                Text("Copy Output")
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