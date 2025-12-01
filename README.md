# Mobile App

A simple Android application with login, dashboard, profile, and logout functionality.

## Features

- Login screen with username and password validation
- Dashboard screen with navigation options
- Profile screen displaying user information
- Logout functionality to return to login screen

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/mobileapp/
│   │   ├── LoginActivity.java     # Login screen
│   │   ├── DashboardActivity.java # Dashboard screen
│   │   └── ProfileActivity.java   # Profile screen
│   ├── res/
│   │   ├── layout/                # UI layouts
│   │   │   ├── activity_login.xml
│   │   │   ├── activity_dashboard.xml
│   │   │   └── activity_profile.xml
│   │   └── values/                # Strings, colors, styles
│   │       ├── strings.xml
│   │       ├── colors.xml
│   │       └── styles.xml
│   └── AndroidManifest.xml
```

## How to Run

1. Open the project in Android Studio
2. Build and run the application on an emulator or physical device
3. Use any non-empty username and password to login

## Notes

- This is a basic implementation with simple credential validation
- In a real application, you would connect to a backend server for authentication