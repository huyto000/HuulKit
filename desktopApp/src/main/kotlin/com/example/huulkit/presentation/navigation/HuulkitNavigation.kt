package com.example.huulkit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.presentation.ui.components.MouthHelperScreen
import com.example.huulkit.presentation.ui.components.MouthTranslatorScreen
import com.example.huulkit.presentation.viewmodel.TextRefinementViewModel
import com.example.huulkit.presentation.viewmodel.TranslatorViewModel

/**
 * Navigation routes for the application
 */
object HuulkitDestinations {
    const val MOUTH_HELPER_ROUTE = "mouth_helper"
    const val MOUTH_TRANSLATOR_ROUTE = "mouth_translator"
}

/**
 * Navigation controller for the application
 */
class HuulkitNavController {
    var currentRoute by mutableStateOf(HuulkitDestinations.MOUTH_HELPER_ROUTE)
        private set

    fun navigate(route: String) {
        currentRoute = route
    }
}

/**
 * Navigation host for the application
 */
@Composable
fun HuulkitNavHost(
    navController: HuulkitNavController,
    textRefinementViewModel: TextRefinementViewModel,
    translatorViewModel: TranslatorViewModel,
    modifier: Modifier = Modifier
) {
    when (navController.currentRoute) {
        HuulkitDestinations.MOUTH_HELPER_ROUTE -> {
            MouthHelperScreen(viewModel = textRefinementViewModel, modifier = modifier)
        }
        HuulkitDestinations.MOUTH_TRANSLATOR_ROUTE -> {
            MouthTranslatorScreen(viewModel = translatorViewModel, modifier = modifier)
        }
    }
}
