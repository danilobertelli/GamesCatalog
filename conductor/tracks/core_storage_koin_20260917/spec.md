# Specification: Setup Room Local Storage, Domain Models, Repository, and Koin DI

## 1. Overview
This track implements the foundational local persistence layer and dependency injection container for GamesCatalog. In alignment with our offline-first architecture, the application must be capable of managing game entries, statuses, and ratings completely on-device without remote network requirements.

## 2. Functional Requirements
- **Domain Models:**
  - `Game`: Represents a catalogued game with `id` (UUID string), `title`, `overview`, `coverImageUrl`, `platforms`, `status`, `rating` (optional 1-5 integer), and `completionDateEpochMs`.
  - `GameStatus`: Enum representing `WANT_TO_PLAY`, `PLAYING`, `COMPLETED`, `ABANDONED`.
  - Type-safe conversions and validation.
- **Local Persistence (Room):**
  - `GameEntity`: SQLite table `games` storing all attributes.
  - Type converters for list of strings (platforms) and enum status.
  - `GameDao`: CRUD suspended functions and reactive queries:
    - `insertOrUpdate(game: GameEntity)`
    - `delete(id: String)`
    - `getGameById(id: String): Flow<GameEntity?>`
    - `getAllGames(): Flow<List<GameEntity>>`
    - `getGamesByStatus(status: String): Flow<List<GameEntity>>`
  - `GamesCatalogDatabase`: Abstract RoomDatabase instance.
- **Repository Pattern:**
  - `GameRepository` interface in domain layer exposing clean domain models via Kotlin `Flow`.
  - `GameRepositoryImpl` in data layer mapping between `GameEntity` and `Game`.
- **Dependency Injection (Koin):**
  - `databaseModule`: Provides Room database instance and DAOs.
  - `repositoryModule`: Binds `GameRepository` to `GameRepositoryImpl`.
  - `appModule`: Combines sub-modules.
  - Custom `GamesCatalogApplication` initialized with `startKoin`.

## 3. Non-Functional Requirements
- **Native Android minSdk 36:** Leverage current Coroutines, Flow, and standard libraries without desugaring.
- **Thread Safety:** Room and repository calls must execute on background dispatchers (`Dispatchers.IO`).
- **Testability:** In-memory Room database setup for unit and repository tests.

## 4. Acceptance Criteria
- Dependencies for Room (with KSP) and Koin compile cleanly.
- Unit tests for `GameDao` with in-memory Room database pass.
- Unit tests for `GameRepositoryImpl` with domain mapping pass.
- `GamesCatalogApplication` starts Koin cleanly without missing dependency definitions.
- Automated test command `./gradlew testDebugUnitTest` completes successfully with 0 failures.

## 5. Out of Scope
- IGDB remote API networking client (to be implemented in a dedicated subsequent track).
- Full Jetpack Compose UI screens (domain and data foundation only).
