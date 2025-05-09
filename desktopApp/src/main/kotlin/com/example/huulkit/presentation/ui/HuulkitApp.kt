package com.example.huulkit.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.huulkit.di.AppContainer
import com.example.huulkit.presentation.theme.HuulkitTheme
import com.example.huulkit.presentation.ui.components.ConfigDialog
import com.example.huulkit.presentation.ui.components.MouthHelperScreen
import com.example.huulkit.presentation.ui.components.MouthTranslatorScreen
import com.example.huulkit.presentation.ui.components.NavigationSidebar

/**
 * Main UI component for the Huulkit application
 */
@Composable
fun HuulkitApp() {
    // Get view models from the AppContainer
    val mainViewModel = AppContainer.mainViewModel
    val textRefinementViewModel = AppContainer.textRefinementViewModel
    val configViewModel = AppContainer.configViewModel
    val translatorViewModel = AppContainer.translatorViewModel

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
                configViewModel = configViewModel
            )

            // Main content area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                when (mainViewModel.selectedTab) {
                    // Mouth helper tab
                    0 -> {
                        MouthHelperScreen(viewModel = textRefinementViewModel)
                    }

                    // Mouth translator tab
                    1 -> {
                        MouthTranslatorScreen(viewModel = translatorViewModel)
                    }
                }
            }
        }

        // API Key Configuration Dialog
        ConfigDialog(configViewModel = configViewModel)
    }
}
