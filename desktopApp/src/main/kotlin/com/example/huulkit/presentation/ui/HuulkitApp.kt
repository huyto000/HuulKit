package com.example.huulkit.presentation.ui

//import com.example.huulkit.presentation.viewmodel.MainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huulkit.presentation.theme.HuulkitTheme
import com.example.huulkit.presentation.ui.components.ConfigDialog
import com.example.huulkit.presentation.ui.components.MouthHelperScreen
import com.example.huulkit.presentation.ui.components.MouthTranslatorScreen
import com.example.huulkit.presentation.ui.components.NavigationSidebar
import com.example.huulkit.presentation.viewmodel.ConfigViewModel
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

/**
 * Main UI component for the Huulkit application
 */
@Composable
fun HuulkitApp() {
    // Get view models from Koin
//    val mainViewModel = koinInject<MainViewModel>()
    val textRefinementViewModel = koinInject<TextRefinementViewModel>()
    val configViewModel = koinInject<ConfigViewModel>()
    val translatorViewModel = koinInject<TranslatorViewModel>()

    val navController = rememberNavController()

    // Initialize the config view model
    LaunchedEffect(Unit) {
        configViewModel.initialize()
    }

    HuulkitTheme {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left sidebar with tabs
            NavigationSidebar(
                configViewModel = configViewModel,
                navController = navController
            )

            // Main content area using the navigation system
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                NavHost(navController = navController, startDestination = Helper) {
                    composable<Helper> {
                        MouthHelperScreen(viewModel = textRefinementViewModel, modifier = Modifier)
                    }
                    composable<Translator> {
                        MouthTranslatorScreen(viewModel = translatorViewModel, modifier = Modifier)
                    }
                }
            }
        }
    }

        // API Key Configuration Dialog
        ConfigDialog(configViewModel = configViewModel)
    }

@Serializable
object Helper
@Serializable
object Translator
