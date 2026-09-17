# Specification: GamesCatalogScreen UI with Search, Filtered List, and FAB

## 1. Overview
Implement the main screen (`GamesCatalogScreen`) displaying the user's game library. The screen features a top search bar, an alphabetically ordered list of games with ratings, a placeholder for cover art, empty/loading states, and a Floating Action Button (FAB) to trigger game creation.

## 2. Functional Requirements
- **Data Source & Sorting:** Retrieve games reactively from `GameRepository` and present them ordered alphabetically by title (A-Z).
- **Reactive Search Bar:** Top search bar supporting instant filtering as the user types, updating the displayed list via reactive StateFlow.
- **Game Item Layout:**
  - Game cover placeholder (styled Material 3 Box / Icon).
  - Game title.
  - Rating displayed below title in "x/5" format (e.g., "4.5/5" or "-/5" if unrated).
- **Floating Action Button (FAB):** Material 3 FAB invoking an `onAddGameClick: () -> Unit` callback.
- **UI States (UDF):**
  - **Loading:** Progress indicator while initial data is being loaded.
  - **Empty (Catalog):** Informative message when no games have been registered yet ("No games registered yet").
  - **Empty (Search):** Informative message when a search query returns no matching games ("No games found for '<query>'").
  - **Success:** Scrollable LazyColumn displaying the filtered game items.

## 3. Technical & Architectural Requirements
- **Architecture:** Unidirectional Data Flow (UDF) with `GamesCatalogViewModel` exposing `StateFlow<GamesCatalogUiState>`.
- **Dependency Injection:** ViewModel injected via Koin (`viewModelOf(::GamesCatalogViewModel)` in `AppModule.kt`).
- **Testing (TDD):** Unit tests for `GamesCatalogViewModel` verifying search filtering, alphabetical sorting, and state transitions using Coroutines Test and Turbine.
- **UI Toolkit:** Jetpack Compose with Material 3 design tokens and theme previews.

## 4. Out of Scope
- Coil remote network image loading (deferred to IGDB API track).
- Add Game Form Screen (will be a dedicated subsequent track).
