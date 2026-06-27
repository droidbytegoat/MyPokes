# MyPokes

A modern Pokédex built with Jetpack Compose, allowing you to browse all existing Pokémon, favorite your picks, and customize the app theme!

## Features

**Pokémon List**
On launch, the user sees a grid of all Pokémon loaded via infinite scroll (20 per page). Each card dynamically extracts the dominant color from the Pokémon's image to compose its background.
Pokémon can be searched by name with autocomplete directly from the list screen.

**Pokémon Detail**
Tapping a Pokémon opens the detail screen with a shared element transition on the image. The screen displays types (as colored chips), base stats with progress bars, height and weight. The image can be tapped to open an animated zoom dialog with a spring effect.

**Favorites**
Pokémon can be favorited from both the list and detail screens. Favorites are persisted locally and accessible from the Favorites screen.

**Settings**
The app supports Light, Dark, and Follow System themes, with dynamic color support on Android 12+. The preference is saved with DataStore.

## Setup

1. Install Android Studio;
2. Open the project;
3. Sync the project with Gradle files;
4. Run the app on an emulator or physical device (minimum Android 8 — API 26).

## API

The API used is **PokéAPI V2**.
(https://pokeapi.co/)

## Architecture

The app follows **Clean Architecture** with a three-module structure:

- **`:app`** — presentation layer (Compose UI, ViewModels)
- **`:domain`** — business logic (use cases, domain models, repository interfaces)
- **`:data`** — data access (HTTP client, local database, mappers, repository implementations)

The state management pattern is **MVI (Model-View-Intent)** with unidirectional data flow:

```
Intent (user action) → Reducer (pure new state) → Middleware (side effects) → UI
```

## Main Dependencies

**Ktor Client** — _HTTP requests_
Modern HTTP client with native coroutines support and Kotlinx serialization integration.

**Kotlinx Serialization** — _JSON serialization_
Kotlin-first serialization solution, used alongside Ktor.

**Jetpack Compose** — _declarative UI_
The entire UI is built with Compose + Material Design 3, including animations, transitions, and edge-to-edge support.

**Hilt** — _dependency injection_
Google's official DI solution built on Dagger, with Compose Navigation integration.

**Room** — _data persistence_
Local database for caching Pokémon and storing favorites, with reactive DAOs via Flow.

**DataStore** — _preferences_
Stores user preferences (theme), replacing SharedPreferences.

**Coil** — _image loading_
Async image loading with Compose support and dominant color extraction via the Palette API.

**Shared Element Transitions** — _navigation animations_
The Pokémon image animates between list and detail screens using Compose's `SharedTransitionLayout` API.

## Build Automation

The project uses build optimizations configured in `gradle.properties`:

- Parallel builds and build cache enabled
- Configuration cache enabled (biggest win for incremental builds)
- Kotlin incremental compilation
- Unnecessary build features disabled (BuildConfig, AIDL, RenderScript, etc.)

ProGuard configured for release builds with preservation rules for Room, Hilt, Ktor, and Kotlinx Serialization models.

## Testing

The project has **17 test files** spread across all three layers:

- **Domain:** use case tests with fake repositories
- **Data:** repository and mapper tests
- **Presentation:** Reducer (pure function) and Middleware (side effects) tests for all screens

Tools used: **MockK**, **Turbine** (for Flows), and **Coroutines Test**.