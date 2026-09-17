# Specification: IGDB API Integration (Retrofit, Twitch Auth, Coil, Remote Search)

## Overview
Connect GamesCatalog to the official **IGDB API v4** using **Retrofit**, **OkHttp**, **kotlinx.serialization**, and **Coil Compose**. This enables remote game cover rendering in the catalog and detail screens, Twitch OAuth2 client credentials token management, and live game search suggestions in `AddGameScreen` to auto-fill title, summary, platforms, and cover art.

---

## Functional Requirements

### 1. Credentials Configuration
- Read `IGDB_CLIENT_ID` and `IGDB_CLIENT_SECRET` from `local.properties`.
- Inject `BuildConfig.IGDB_CLIENT_ID` and `BuildConfig.IGDB_CLIENT_SECRET` into `app/build.gradle.kts`.

### 2. Twitch OAuth2 Token Management
- Authenticate via `POST https://id.twitch.tv/oauth2/token?client_id=...&client_secret=...&grant_type=client_credentials`.
- Parse `TwitchTokenResponse` containing `access_token`, `expires_in`, and `token_type`.
- Cache the bearer token in memory with expiry validation so repeated calls reuse the valid token.

### 3. IGDB API Service (Retrofit + Apicalypse)
- Base URL: `https://api.igdb.com/v4/`.
- Endpoint: `POST games` with plain text Apicalypse body:
  ```apicalypse
  fields name, summary, cover.image_id, platforms.name, first_release_date, total_rating;
  search "{query}";
  limit 20;
  ```
- OkHttp Interceptor adding `Client-ID: <IGDB_CLIENT_ID>` and `Authorization: Bearer <access_token>`.
- Parse response into DTOs (`IgdbGameDto`, `IgdbCoverDto`, `IgdbPlatformDto`).
- Format cover image URL using IGDB's standard structure:
  `https://images.igdb.com/igdb/image/upload/t_cover_big/{image_id}.jpg`

### 4. Coil Image Loading in Jetpack Compose
- Integrate `coil-compose` in `GameListItem` to display the game cover image when `coverImageUrl` is present, with fallback to the existing `VideogameAsset` placeholder.
- Integrate `AsyncImage` in `GameDetailScreen` to display the high-res cover art.

### 5. Remote Search in AddGameScreen
- In `AddGameScreen`, when typing in the title field with at least 3 characters, query IGDB for matching games (debounced).
- Display suggestions dropdown/list showing title, release year, platforms, and thumbnail cover.
- Tapping a suggestion auto-populates:
  - Title
  - Notes & Overview (from IGDB `summary`)
  - Cover Image URL (from IGDB `cover.image_id`)
  - Pre-selects matching platforms if available in local `Platform` database.

---

## Non-Functional Requirements
- **Architecture:** Clean Architecture + UDF. Network models (`*Dto`) mapped to domain models or search results in repository layer.
- **TDD:** Unit tests for token management, network mapping, Apicalypse query generation, and ViewModel search debouncing.
- **Security:** API keys strictly kept in `local.properties`, never hardcoded or committed to git.
- **Strings:** All new UI labels, error messages, and placeholders localized in `res/values/strings.xml`.
- **Graceful Error Handling:** Network errors, rate limits, or missing internet must not crash the app, displaying non-intrusive error feedback.

---

## Acceptance Criteria
- [ ] Dependencies configured in `libs.versions.toml` and `app/build.gradle.kts`.
- [ ] Unit tests pass for `TwitchAuthRepository` / `TokenManager` and `IgdbRemoteDataSource`.
- [ ] Coil loads and displays covers in `GameListItem` and `GameDetailScreen`.
- [ ] `AddGameScreen` provides remote search suggestions and auto-fills selected game data.
- [ ] All unit tests pass with `./gradlew testDebugUnitTest`.
- [ ] Debug APK builds and installs cleanly on connected device (`XT2603-1`).
