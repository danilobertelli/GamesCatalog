# Technology Stack

## 1. Platform & Environment
- **Target OS:** Android
- **Minimum SDK (minSdk):** 36
- **Target SDK (targetSdk):** 37
- **Compile SDK (compileSdk):** 37
- **Language:** Kotlin 2.x
- **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`) with Version Catalogs (`gradle/libs.versions.toml`)
- **Note on Backward Compatibility:** With `minSdk = 36`, the codebase does not need backward compatibility checks for legacy Android versions (e.g., desugaring or legacy API guards). It leverages state-of-the-art modern Android runtime APIs directly.

## 2. Core Architecture & Layers
Adopts **Clean Architecture** with **Unidirectional Data Flow (UDF)**:
- **Presentation Layer:** Jetpack Compose, Material 3, ViewModels exposing immutable `StateFlow<UiState>` and receiving user intents via events.
- **Domain Layer:** Pure Kotlin business logic, Models, and UseCases/Interactors.
- **Data Layer:** Repository pattern with single-source-of-truth strategy:
  - **Local Data Source:** Room Database with Coroutines and Flow observables.
  - **Remote Data Source:** IGDB API client using Retrofit / OkHttp or Ktor with Kotlinx Serialization.
- **Observability & Code Quality:**
  - Structured Android Logging (`Log.d`, `Log.e`) across network, auth, and persistence boundaries without logging PII/credentials.
  - Full KDoc coverage across public contracts, domain models, and DAOs.

## 3. Libraries & Dependencies
- **UI & Toolkit:**
  - `androidx.compose.ui`, `androidx.compose.material3`, `androidx.compose.foundation`
  - `coil-compose` for asynchronous image and cover art loading
- **Persistence:**
  - `androidx.room:room-runtime`, `androidx.room:room-ktx`, `androidx.room:room-compiler` (KSP)
- **Networking & Serialization:**
  - `com.squareup.retrofit2:retrofit` or Ktor Client
  - `org.jetbrains.kotlinx:kotlinx-serialization-json`
  - `com.squareup.okhttp3:logging-interceptor`
- **Dependency Injection:**
  - `io.insert-koin:koin-androidx-compose` (Koin as the primary DI framework for Android and Compose)
- **Remote Integration:**
  - IGDB API v4 (Twitch OAuth2 token exchange + Protocol Buffers / JSON endpoints)

## 4. Testing Stack
- **Unit Testing:** JUnit 5 / JUnit 4, Kotlin Coroutines Test (`kotlinx-coroutines-test`)
- **Flow & State Testing:** Turbine for observing Flow emissions
- **Mocking / Test Doubles:** MockK and in-memory repository fakes
- **UI / Integration Testing:** `androidx.compose.ui.test.junit4`, Room In-Memory Database
