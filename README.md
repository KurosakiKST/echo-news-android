# Echo - News App

Echo is a modern Android news application that demonstrates best practices in Android development. Built with MVVM architecture, modular design, dependency injection, and offline-first approach, Echo provides a seamless news reading experience.

## Features

- Browse top headlines by category
- Search for news on specific topics
- Bookmark articles for later reading
- Offline support with local caching
- Clean and intuitive Material Design UI

## Architecture

Echo follows a modular, clean architecture approach:

```
app/                          # Application module with main activity and navigation
core/                         # Core module with all foundational functionality
  ├── common/                 # Common utilities and extensions
  ├── data/                   # Data layer with repositories and data sources
  ├── database/               # Local database with Room
  ├── domain/                 # Domain layer with models and use cases
  ├── network/                # Network layer with Retrofit
feature/                      # Feature module containing all app features
  ├── home/                   # Home screen to browse top headlines
  ├── search/                 # Search functionality
  ├── bookmark/               # Bookmarked articles
  ├── detail/                 # Article detail screen
  └── components/             # Shared UI components
```

### Tech Stack

- **Architecture:** MVVM with Clean Architecture
- **UI:** Jetpack Compose with Material 3
- **Dependency Injection:** Dagger Hilt
- **Networking:** Retrofit
- **Local Storage:** Room Database
- **State Management:** Kotlin Flow and StateFlow
- **Image Loading:** Coil
- **Concurrency:** Kotlin Coroutines
- **Building:** Gradle with Kotlin DSL

## Getting Started

### Prerequisites

- Android Studio Ladybug or newer
- Min SDK: 29 (Android 10)
- Kotlin 1.9.0+
- JDK 17

### Setup

1. Clone the repository:
   ```
   git clone https://github.com/KurosakiKSt/echo-news-android.git
   ```

2. Get a News API key from [newsapi.org](https://newsapi.org)

3. Add your API key to `local.properties`:
   ```
   news.api.key=YOUR_API_KEY
   ```

4. Build and run the app from Android Studio

## Architecture Details

### Data Flow

1. **View (UI)** - Composable screens that display data and handle user interactions
2. **ViewModel** - Manages UI state and processes user events
3. **UseCase** - Contains business logic and coordinates data operations
4. **Repository** - Acts as a single source of truth for data
5. **Data Sources** - Remote API and local database

### Unidirectional Data Flow

Echo implements a unidirectional data flow pattern:
- State flows down from ViewModel to UI
- Events flow up from UI to ViewModel
- ViewModels update state based on events and data changes

### Offline First

Echo follows an offline-first approach:
- Data is always fetched from the local database first
- Database is updated with remote data when available
- Users can view cached content when offline

## Acknowledgments

- [News API](https://newsapi.org/) for providing the news data
- Android Jetpack libraries
- All open-source libraries used in this project
