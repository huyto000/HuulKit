package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.huulkit.presentation.theme.HuulkitBlue
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel

/**
 * Screen for the Mouth Translator tab
 */
@Composable
fun MouthTranslatorScreen(
    viewModel: TranslatorViewModel,
    modifier: Modifier = Modifier
) {
    val translatorScrollState = rememberScrollState()
    
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
                }
                
                OutlinedTextField(
                    value = viewModel.englishText,
                    onValueChange = { viewModel.updateEnglishText(it) },
                    label = { Text("Enter English text") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 150.dp)
                )
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
                }
                
                OutlinedTextField(
                    value = viewModel.swedishText,
                    onValueChange = { viewModel.updateSwedishText(it) },
                    label = { Text("Enter Swedish text") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 150.dp)
                )
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
                }
                
                OutlinedTextField(
                    value = viewModel.vietnameseText,
                    onValueChange = { viewModel.updateVietnameseText(it) },
                    label = { Text("Enter Vietnamese text") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 150.dp)
                )
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
                    shape = MaterialTheme.shapes.medium
                ) {
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