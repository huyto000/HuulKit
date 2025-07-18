package com.example.huulkit.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.huulkit.presentation.navigation.HuulkitDestinations
import com.example.huulkit.presentation.navigation.HuulkitNavController
import com.example.huulkit.presentation.viewmodel.ConfigViewModel

/**
 * Navigation sidebar component with tabs using standard NavigationRail
 */
@Composable
fun NavigationSidebar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    configViewModel: ConfigViewModel,
    modifier: Modifier = Modifier,
    navController: HuulkitNavController? = null
) {
    NavigationRail(
        modifier = modifier.width(150.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 4.dp
    ) {
        // App logo with padding to create space
        Image(
            painter = painterResource("icon.png"),
            contentDescription = "Huulkit Logo",
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(64.dp)
        )

        if (navController != null) {
            // Use navigation routes
            NavigationRailItem(
                icon = { 
                    Icon(
                        Icons.Filled.Home, 
                        contentDescription = "Mouth helper",
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Mouth helper",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ) 
                },
                selected = navController.currentRoute == HuulkitDestinations.MOUTH_HELPER_ROUTE,
                onClick = { navController.navigate(HuulkitDestinations.MOUTH_HELPER_ROUTE) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .let {
                        if (navController.currentRoute == HuulkitDestinations.MOUTH_HELPER_ROUTE) {
                            it.clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                        } else {
                            it
                        }
                    }
            )

            NavigationRailItem(
                icon = { 
                    Icon(
                        Icons.Filled.Translate, 
                        contentDescription = "Mouth translator",
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Mouth translator",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ) 
                },
                selected = navController.currentRoute == HuulkitDestinations.MOUTH_TRANSLATOR_ROUTE,
                onClick = { navController.navigate(HuulkitDestinations.MOUTH_TRANSLATOR_ROUTE) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .let {
                        if (navController.currentRoute == HuulkitDestinations.MOUTH_TRANSLATOR_ROUTE) {
                            it.clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                        } else {
                            it
                        }
                    }
            )
        } else {
            // Fallback to legacy tab selection
            NavigationRailItem(
                icon = { 
                    Icon(
                        Icons.Filled.Home, 
                        contentDescription = "Mouth helper",
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Mouth helper",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ) 
                },
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .let {
                        if (selectedTab == 0) {
                            it.clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                        } else {
                            it
                        }
                    }
            )

            NavigationRailItem(
                icon = { 
                    Icon(
                        Icons.Filled.Translate, 
                        contentDescription = "Mouth translator",
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Mouth translator",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ) 
                },
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .let {
                        if (selectedTab == 1) {
                            it.clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                        } else {
                            it
                        }
                    }
            )
        }

        // Settings button
        NavigationRailItem(
            icon = { 
                Icon(
                    Icons.Filled.Settings, 
                    contentDescription = "Configure Gemini API Key",
                    modifier = Modifier.padding(bottom = 8.dp)
                ) 
            },
            label = { 
                Text(
                    "Settings",
                    color = Color.White,
                    textAlign = TextAlign.Center
                ) 
            },
            selected = false,
            onClick = { configViewModel.showDialog() },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
