package com.example.huulkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.TrayState

/**
 * Manages system tray functionality for the application
 */
class SystemTrayManager {
    companion object {
        /**
         * Creates a system tray icon with a menu
         */
        @Composable
        fun createTray(
            applicationScope: ApplicationScope,
            trayState: TrayState,
            windowVisibility: MutableState<Boolean>,
            onExitRequest: () -> Unit
        ) {
            val icon = painterResource("icon.png")
            applicationScope.Tray(
                state = trayState,
                icon = icon,
                tooltip = "Huulkit Application",
                onAction = { windowVisibility.value = true }
            ) {
                Item(
                    "Open Huulkit",
                    onClick = { windowVisibility.value = true }
                )
                Item(
                    "Exit",
                    onClick = onExitRequest
                )
            }
        }

        /**
         * Shows a notification in the system tray
         */
        fun showNotification(trayState: TrayState, title: String, message: String) {
            trayState.sendNotification(
                Notification(
                    title = title,
                    message = message
                )
            )
        }
    }
}
