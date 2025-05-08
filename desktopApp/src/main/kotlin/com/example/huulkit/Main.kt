package com.example.huulkit

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.huulkit.ui.HuulkitApp // This will be created next

fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(800.dp, 700.dp),
        position = WindowPosition(Alignment.Center)
    )

    androidx.compose.ui.window.Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Huulkit: Make your mouth better"
    ) {
        HuulkitApp() // The main UI content
    }
} 