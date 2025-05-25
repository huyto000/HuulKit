package com.example.huulkit

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import com.example.huulkit.di.appModule
import com.example.huulkit.presentation.ui.HuulkitApp
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

fun main() = application {
    // Initialize Koin
    stopKoin()
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

    Tray(
        state = trayState,
        icon = icon,
        tooltip = "Huulkit Application",
        onAction = { isVisible.value = true }
    ) {
        Item(
            "Open Huulkit",
            onClick = { isVisible.value = true }
        )
        Item(
            "Exit",
            onClick = ::exitApplication
        )
    }

    if(isVisible.value) {
        println("Show window")
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
