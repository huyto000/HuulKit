# Huulkit Application

## System Tray Functionality

The Huulkit application includes system tray functionality, which allows it to:

1. Continue running in the background even when the main window is closed
2. Show an icon in the system tray for quick access
3. Provide a context menu with options to:
   - Open the main application window
   - Exit the application completely

### Usage

- When you click the "X" (close) button, the application window will be hidden, but the application will continue running in the background.
- To access the application again, click on the Huulkit icon in your system tray.
- To completely exit the application, right-click on the system tray icon and select "Exit".

### About the Icon

The application uses a custom icon (`icon.png`) located in the resources directory. This icon appears in:
- The application window
- The taskbar
- The system tray

## Troubleshooting

If you don't see the system tray icon:
- Make sure your system supports system tray icons
- Check if you have other applications that might be hiding system tray icons
- Restart the application
