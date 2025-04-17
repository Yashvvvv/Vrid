# Vrid - Android Blog Application

A modern Android blog application built with Jetpack Compose and following best practices in Android development.

## Features

- **Blog List**: Display a list of blog posts with pagination support
- **Blog Detail**: View detailed blog post content with HTML rendering
- **Dark Mode Support**: Toggle between light and dark themes
- **Offline Support**: Cache blog posts for offline viewing
- **Modern UI**: Built with Material 3 design using Jetpack Compose

## Technology Stack

### Architecture
- **MVVM Architecture**: Separation of UI, business logic, and data layers
- **Clean Architecture**: Well-organized code with clear boundaries between layers
- **Repository Pattern**: Single source of truth for data access

### Android Jetpack Components
- **Jetpack Compose**: Declarative UI toolkit for Android
- **ViewModel**: Manage UI-related data in a lifecycle-conscious way
- **Room**: SQLite database with abstraction layer for local caching
- **Navigation Compose**: Handle in-app navigation
- **Hilt**: Dependency injection
- **Flow**: Asynchronous data streams with coroutines

### Networking
- **Retrofit**: Type-safe HTTP client
- **OkHttp**: HTTP client for network requests
- **Moshi**: JSON parser

### Other Libraries
- **Coil**: Image loading library for Compose
- **Splash Screen API**: Modern splash screen implementation
- **Edge-to-edge UI**: Immersive UI experience

## Project Structure

```
app/src/main/java/app/recruit/vrid/
├── data/
│   ├── api/          # Network API interfaces
│   ├── local/        # Room database and DAOs
│   ├── model/        # Data models
│   └── repository/   # Repository implementations
├── di/               # Dependency injection modules
├── ui/
│   ├── blog/         # Blog screens and viewmodels
│   ├── components/   # Reusable UI components
│   ├── navigation/   # Navigation setup
│   └── theme/        # Theming and styling
├── util/             # Utility classes
├── MainActivity.kt   # Main entry point
└── VridApplication.kt # Application class
```

## Setup and Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Minimum SDK: 34 (Android 14)
- Target SDK: 34
- Kotlin 1.9.0+

### Building the Project
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on an emulator or physical device

## API Integration

The app connects to a WordPress REST API at `https://blog.vrid.in/wp-json/wp/v2/` for fetching blog posts. It supports:
- Pagination
- Post details
- Content rendering

## Performance Considerations

- **Offline Caching**: Blog posts are cached in a Room database for offline access
- **Pagination**: Only loads necessary data to reduce network usage
- **Image Loading**: Efficient image loading with Coil
- **Lazy Loading**: UI elements are loaded lazily using Compose LazyColumn

## Future Improvements

- User authentication
- Commenting functionality
- Bookmarking favorite posts
- Push notifications for new posts
- Search functionality

## License

This project is licensed under the MIT License - see below for details:

MIT License

Copyright (c) 2024