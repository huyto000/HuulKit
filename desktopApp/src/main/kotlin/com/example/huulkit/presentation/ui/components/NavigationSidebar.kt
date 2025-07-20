package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.huulkit.presentation.ui.Helper
import com.example.huulkit.presentation.ui.Translator
import com.example.huulkit.presentation.viewmodel.ConfigViewModel

/**
 * Navigation sidebar component with tabs
 */
@Composable
fun NavigationSidebar(
    selectedTab: Int = 0,
    configViewModel: ConfigViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController? = null
) {
    // Manage selected tab state internally
    val (currentTab, setCurrentTab) = remember { mutableStateOf(selectedTab) }

    // Observe navigation changes and update selected tab
    if (navController != null) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Update selected tab based on current route
        LaunchedEffect(currentRoute) {
            when (currentRoute) {
                Helper.toString() -> setCurrentTab(0)
                Translator.toString() -> setCurrentTab(1)
            }
        }
    }

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

        NavigationTab(
            title = "Mouth helper",
            icon = Icons.Filled.Healing,
            selected = currentTab == 0,
            onClick = { 
                setCurrentTab(0)
                navController?.navigate(Helper) 
            }
        )

        // Mouth translator tab
        NavigationTab(
            title = "Mouth translator",
            icon = Icons.Filled.Translate,
            selected = currentTab == 1,
            onClick = { 
                setCurrentTab(1)
                navController?.navigate(Translator) 
            }
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
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
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
                    .height(32.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(12.dp))
        } else {
            Spacer(modifier = Modifier.width(16.dp))
        }

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.body1,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
