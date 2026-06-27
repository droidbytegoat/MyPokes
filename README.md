# MyPokes

A modern Pokédex built with Jetpack Compose, allowing you to browse all existing Pokémon, favorite your picks, and customize the app theme!

<img width="270" height="600" alt="Screenshot_1782589336" src="https://github.com/user-attachments/assets/25fed4a1-586b-4b54-8294-bc57c958ba42" />
<img width="270" height="600" alt="Screenshot_1782589340" src="https://github.com/user-attachments/assets/8a6532d9-e654-4839-a745-e6370b498cb8" />
<img width="270" height="600" alt="Screenshot_1782587860" src="https://github.com/user-attachments/assets/04bed241-ebb3-4957-a1fb-386d8623349b" />
<img width="270" height="600" alt="Screenshot_1782587871" src="https://github.com/user-attachments/assets/f1b1bd82-8c8d-406e-ace7-1460d7c858d0" />
<img width="270" height="600" alt="Screenshot_1782587991" src="https://github.com/user-attachments/assets/83e440cc-63ac-415d-b71c-22166eda55f0" />
<img width="270" height="600" alt="Screenshot_1782587993" src="https://github.com/user-attachments/assets/6dc967c4-d6ac-41cf-80fc-8ccae4ca33d7" />
<img width="270" height="600" alt="Screenshot_1782587996" src="https://github.com/user-attachments/assets/278122ec-8e35-4c6b-a5e2-3cd41355d987" />
<img width="270" height="600" alt="Screenshot_1782587997" src="https://github.com/user-attachments/assets/f3611e0b-2434-42da-b837-8ff748e9b636" />
<img width="270" height="600" alt="Screenshot_1782587999" src="https://github.com/user-attachments/assets/3fbe154f-4f8c-4c85-ade7-64c20934a7ce" />



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
