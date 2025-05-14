package com.example.huulkit.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.huulkit.presentation.theme.HuulkitTheme
import com.example.huulkit.presentation.ui.components.ConfigDialog
import com.example.huulkit.presentation.ui.components.MouthHelperScreen
import com.example.huulkit.presentation.ui.components.MouthTranslatorScreen
import com.example.huulkit.presentation.ui.components.NavigationSidebar
import com.example.huulkit.presentation.viewmodel.ConfigViewModel
import com.example.huulkit.presentation.viewmodel.MainViewModel
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel
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
