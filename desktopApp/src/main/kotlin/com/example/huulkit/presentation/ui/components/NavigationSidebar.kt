package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.huulkit.presentation.viewmodel.ConfigViewModel

/**
 * Navigation sidebar component with tabs
 */
@Composable
fun NavigationSidebar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    configViewModel: ConfigViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary)
            .selectableGroup()
            .padding(top = 16.dp)
    ) {
        // App logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource("icon.png"),
                contentDescription = "Huulkit Logo",
                modifier = Modifier.size(96.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Mouth helper tab
        NavigationTab(
            title = "Mouth helper",
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        
        // Mouth translator tab
        NavigationTab(
            title = "Mouth translator",
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Settings button
        IconButton(
            onClick = { configViewModel.showDialog() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Configure Gemini API Key",
                tint = Color.White
            )
        }
    }
}

/**
 * Individual navigation tab
 */
@Composable
private fun NavigationTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(12.dp))
        } else {
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.body1,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}