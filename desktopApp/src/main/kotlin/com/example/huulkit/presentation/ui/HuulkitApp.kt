package com.example.huulkit.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.huulkit.presentation.theme.HuulkitTheme
import com.example.huulkit.presentation.ui.components.ConfigDialog
import com.example.huulkit.presentation.ui.components.NavigationSidebar
import com.example.huulkit.presentation.viewmodel.ConfigViewModel
import com.example.huulkit.presentation.viewmodel.MainViewModel
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel
import com.example.huulkit.presentation.navigation.HuulkitNavHost
import org.koin.compose.koinInject

/**
 * Main UI component for the Huulkit application
 */
@Composable
fun HuulkitApp() {
    // Get view models from Koin
    val mainViewModel = koinInject<MainViewModel>()
    val textRefinementViewModel = koinInject<TextRefinementViewModel>()
    val configViewModel = koinInject<ConfigViewModel>()
    val translatorViewModel = koinInject<TranslatorViewModel>()

    // Initialize the config view model
    LaunchedEffect(Unit) {
        configViewModel.initialize()
    }

    HuulkitTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left sidebar with tabs
            NavigationSidebar(
                selectedTab = mainViewModel.selectedTab,
                onTabSelected = { mainViewModel.updateSelectedTab(it) },
                configViewModel = configViewModel,
                navController = mainViewModel.navController
            )

            // Main content area using the navigation system
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                HuulkitNavHost(
                    navController = mainViewModel.navController,
                    textRefinementViewModel = textRefinementViewModel,
                    translatorViewModel = translatorViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // API Key Configuration Dialog
        ConfigDialog(configViewModel = configViewModel)
    }
}
