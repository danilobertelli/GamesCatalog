# Implementation Plan: IGDB API Integration (Retrofit, Twitch Auth, Coil, Remote Search)

## Phase 1: Dependencies Setup & BuildConfig Credentials
- [x] Task: Add Retrofit, OkHttp, `retrofit2-kotlinx-serialization-converter`, `kotlinx-serialization-json`, and `coil-compose` to `gradle/libs.versions.toml`
- [x] Task: Apply `kotlinx-serialization` plugin and dependencies in `app/build.gradle.kts`
- [x] Task: Configure `buildFeatures { buildConfig = true }` and read `IGDB_CLIENT_ID` / `IGDB_CLIENT_SECRET` from `local.properties` into `BuildConfig`
- [x] Task: Run `./gradlew testDebugUnitTest` to verify project sync and compilation
- [x] Task: Conductor - User Manual Verification 'Phase 1 - Dependencies & BuildConfig' (Protocol in workflow.md)

## Phase 2: Twitch Auth, IGDB DTOs, Retrofit Service & Remote DataSource (TDD)
- [x] Task: Model `TwitchTokenResponse`, `IgdbGameDto`, `IgdbCoverDto`, and `IgdbPlatformDto` with `@Serializable`
- [x] Task: Create `TwitchAuthService` and `TwitchTokenManager` with token caching and expiry check
- [x] Task: Create `IgdbAuthInterceptor` adding `Client-ID` and `Authorization: Bearer <token>`
- [x] Task: Create `IgdbApiService` defining `POST games` taking `@Body RequestBody` (Apicalypse query)
- [x] Task: Implement `IgdbRemoteDataSource` with helper for constructing `https://images.igdb.com/igdb/image/upload/t_cover_big/{image_id}.jpg`
- [x] Task: Write unit tests in `TwitchTokenManagerTest.kt` and `IgdbRemoteDataSourceTest.kt` using MockWebServer or mock engines
- [x] Task: Register network dependencies in `AppModule.kt` (or new `NetworkModule.kt`) and update `KoinModulesTest.kt`
- [x] Task: Run `./gradlew testDebugUnitTest` to verify all tests pass
- [x] Task: Conductor - User Manual Verification 'Phase 2 - Network & Auth Layer' (Protocol in workflow.md)

## Phase 3: Coil Image Loading in Jetpack Compose
- [x] Task: Update `GameListItem` to use `AsyncImage` with placeholder and error fallbacks
- [x] Task: Update `GameDetailScreen` header to display large cover image via `AsyncImage`
- [x] Task: Add preview tests or verification for Coil composables
- [x] Task: Conductor - User Manual Verification 'Phase 3 - Coil Image Loading' (Protocol in workflow.md)

## Phase 4: Remote Game Search & Autocomplete in AddGameScreen
- [x] Task: Extract search UI strings into `res/values/strings.xml` (search placeholder, suggestions label, clear search, no results)
- [x] Task: Update `AddGameViewModel` with remote search state (`searchQuery`, `searchResults`, `isSearching`, `searchError`) and debounce flow
- [x] Task: Write unit tests in `AddGameViewModelTest.kt` for remote search query, debounce, error handling, and auto-filling game data
- [x] Task: Update `AddGameScreen` to display interactive search suggestions dropdown/list when typing title
- [x] Task: On selecting a suggestion, auto-fill Title, Overview, Cover Image URL, and pre-select matching platforms
- [x] Task: Conductor - User Manual Verification 'Phase 4 - Remote Search & Autocomplete' (Protocol in workflow.md)

## Phase 5: Verification, APK Build & Device Installation
- [x] Task: Run `./gradlew testDebugUnitTest assembleDebug`
- [x] Task: Install APK on connected physical device via `./gradlew installDebug` for developer verification
- [x] Task: Update `ARTICLE_DRAFT.md` with Track 5 architecture and learnings
- [x] Task: Conductor - User Manual Verification 'Phase 5 - Final Verification & Device Install' (Protocol in workflow.md)
