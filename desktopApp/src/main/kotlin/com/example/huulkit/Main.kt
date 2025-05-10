package com.example.huulkit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import com.example.huulkit.presentation.ui.HuulkitApp
import com.example.huulkit.di.appModule
import org.koin.core.context.startKoin

fun main() = application {
    // Initialize Koin
    startKoin {
        modules(appModule)
    }

    val windowState = rememberWindowState(
        size = DpSize(800.dp, 700.dp),
        position = WindowPosition(Alignment.Center)
    )

    val trayState = rememberTrayState()
    val isVisible = remember { mutableStateOf(true) }
    val icon = painterResource("icon.png")

    // Set up system tray
    SystemTrayManager.createTray(
        this,
        trayState = trayState,
        windowVisibility = isVisible,
        onExitRequest = ::exitApplication
    )

    // Show notification when minimized to tray
    if (!isVisible.value) {
        SystemTrayManager.showNotification(
            trayState = trayState,
            title = "Huulkit is still running",
            message = "Application is minimized to system tray"
        )
    }

    if (isVisible.value) {
        androidx.compose.ui.window.Window(
            onCloseRequest = { 
                // Just hide the window instead of exiting
                isVisible.value = false 
            },
            state = windowState,
            title = "Huulkit: Make your mouth better",
            icon = icon
        ) {
            HuulkitApp() // The main UI content
        }
    }
} 
