package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.huulkit.presentation.navigation.HuulkitDestinations
import com.example.huulkit.presentation.navigation.HuulkitNavController

/**
 * ViewModel for the main application state
 */
class MainViewModel {
    // Navigation controller
    val navController = HuulkitNavController()

    // For backward compatibility
    val selectedTab: Int
        get() = when (navController.currentRoute) {
            HuulkitDestinations.MOUTH_HELPER_ROUTE -> 0
            HuulkitDestinations.MOUTH_TRANSLATOR_ROUTE -> 1
            else -> 0
        }

    /**
     * Updates the selected tab
     */
    fun updateSelectedTab(tab: Int) {
        when (tab) {
            0 -> navController.navigate(HuulkitDestinations.MOUTH_HELPER_ROUTE)
            1 -> navController.navigate(HuulkitDestinations.MOUTH_TRANSLATOR_ROUTE)
        }
    }

    /**
     * Navigates to the specified route
     */
    fun navigateTo(route: String) {
        navController.navigate(route)
    }
}
