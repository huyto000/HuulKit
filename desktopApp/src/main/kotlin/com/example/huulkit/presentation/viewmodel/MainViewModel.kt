package com.example.huulkit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * ViewModel for the main application state
 */
class MainViewModel {
    // Tab selection state
    var selectedTab by mutableStateOf(0)
        private set
    
    /**
     * Updates the selected tab
     */
    fun updateSelectedTab(tab: Int) {
        selectedTab = tab
    }
}