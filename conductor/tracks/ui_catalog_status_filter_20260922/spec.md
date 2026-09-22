# Specification: Status Filter Chips in GamesCatalogScreen

## 1. Overview
Implement horizontal status filter chips on the main catalog screen (`GamesCatalogScreen`) allowing users to filter their personal game collection by status: **Todos (All)**, **Quero Jogar (Want to Play)**, **Jogando (Playing)**, **Concluído (Completed)**, and **Abandonado (Abandoned)**.
The state is managed reactively in `GamesCatalogViewModel` following TDD by combining `gameRepository.getAllGames()` with `searchQueryFlow` and `selectedStatusFilterFlow`. The entire track follows a strict single-commit policy in Git.

## 2. Functional Requirements
- **Filter Chips Row:**
  - Horizontal scrollable row (`LazyRow`) displayed directly below the search bar.
  - 5 filter options:
    - **Todos**: displays all games (no status filter applied, default state).
    - **Quero Jogar**: filters games where `status == GameStatus.WANT_TO_PLAY`.
    - **Jogando**: filters games where `status == GameStatus.PLAYING`.
    - **Concluído**: filters games where `status == GameStatus.COMPLETED`.
    - **Abandonado**: filters games where `status == GameStatus.ABANDONED`.
- **Selection Behavior:**
  - Single-select: exactly one filter chip is active at any time.
  - Defaults to "Todos" upon initial catalog load.
- **Combined Filtering & Sorting:**
  - Conjunction (`AND` logic): displayed games must match both the active search query (case-insensitive substring of title) AND the selected status filter.
  - Alphabetical ordering (A-Z) by title is maintained across all filtered views.
- **Empty States:**
  - When the entire catalog has no games: displays standard catalog empty state.
  - When games exist in the library but none match the current query + status filter: displays empty search/filter result state.
- **Strings & Localization:**
  - Filter chip labels defined in `res/values/strings.xml` without hardcoded text in Composables.

## 3. Technical & Architectural Requirements
- **Unidirectional Data Flow (UDF):**
  - `GamesCatalogUiState` updated to include `selectedStatus: GameStatus? = null` (null represents "Todos").
  - `GamesCatalogViewModel` introduces `selectedStatusFlow: MutableStateFlow<GameStatus?>` and an event handler `onStatusFilterSelected(status: GameStatus?)`.
  - Reactive `combine` merges `gameRepository.getAllGames()`, `searchQueryFlow`, and `selectedStatusFlow`.
- **Test-Driven Development (TDD):**
  - Red -> Green -> Refactor cycle in `GamesCatalogViewModelTest`.
  - Unit tests verify filtering by each specific `GameStatus`, combining status filtering with search queries, resetting back to "Todos" (all), and empty filter results.
- **UI & Design System:**
  - Material 3 `FilterChip` components inside a scrollable `LazyRow` with proper padding, elevation, and selected container colors.
  - Updated Compose `@Preview` definitions covering various filter selections.
- **Git Commit Hygiene:**
  - The entire track must result in exactly 1 clean Git commit (subsequent phase checkpoints must use `git commit --amend` when explicitly instructed by the user).

## 4. Acceptance Criteria
- [ ] ViewModel unit tests pass covering single status filtering, combined search+status filtering, and null (all) filter.
- [ ] Status filter chips render cleanly on `GamesCatalogScreen` below the search bar.
- [ ] Tapping any chip immediately updates the list to show only matching games.
- [ ] Tapping "Todos" restores the full list.
- [ ] `./gradlew testDebugUnitTest` passes without errors.
- [ ] Code strictly adheres to project architecture and style guides (no hardcoded strings, structured logging, KDoc comments).

## 5. Out of Scope
- Multi-select status filtering.
- Persistence of selected filter across app relaunches (in-memory per session).
