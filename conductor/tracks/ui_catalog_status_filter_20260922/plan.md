# Implementation Plan: Status Filter Chips in GamesCatalogScreen

## Phase 1: ViewModel & State Layer (TDD)
- [x] Task: Define Status Filter State and Write ViewModel Unit Tests (Red Phase)
    - [x] Add `selectedStatus: GameStatus? = null` property to `GamesCatalogUiState`
    - [x] Write unit tests in `GamesCatalogViewModelTest` covering: filtering games by specific `GameStatus`, combining status filtering with search queries, resetting filter to "Todos" (`null`), and empty state when no games match the status filter
    - [x] Run `./gradlew testDebugUnitTest` and verify tests fail for the expected reason (Red Phase)
- [x] Task: Implement Reactive Status Filtering in GamesCatalogViewModel (Green Phase)
    - [x] Add `selectedStatusFlow = MutableStateFlow<GameStatus?>(null)` and `onStatusFilterSelected(status: GameStatus?)` in `GamesCatalogViewModel`
    - [x] Update the `combine` block to include `selectedStatusFlow` with `gameRepository.getAllGames()` and `searchQueryFlow`
    - [x] Run `./gradlew testDebugUnitTest` and verify all tests pass (Green Phase)
- [x] Task: Conductor - User Manual Verification 'Phase 1: ViewModel & State Layer (TDD)' (Protocol in workflow.md)

## Phase 2: UI Components & Localization
- [x] Task: Add String Resources for Status Chips
    - [x] Define string resources in `res/values/strings.xml` for "Todos", "Quero Jogar", "Jogando", "Concluído", and "Abandonado"
- [x] Task: Implement StatusFilterChips Component
    - [x] Create `StatusFilterChips.kt` composable using Material 3 `FilterChip` components in a horizontally scrollable `LazyRow`
    - [x] Apply proper spacing, padding, selected container colors, and accessibility labels
    - [x] Add Compose `@Preview` for `StatusFilterChips` demonstrating active and inactive states
- [x] Task: Conductor - User Manual Verification 'Phase 2: UI Components & Localization' (Protocol in workflow.md)

## Phase 3: Screen Integration & Compose Previews
- [x] Task: Integrate StatusFilterChips into GamesCatalogScreen
    - [x] Place `StatusFilterChips` inside `GamesCatalogContent` top section between `CatalogSearchBar` and the catalog list
    - [x] Connect `onStatusFilterSelected` callback from `GamesCatalogScreen` to `GamesCatalogViewModel`
    - [x] Update Compose Previews in `GamesCatalogScreen.kt` to illustrate populated catalog with active status filter
- [x] Task: Conductor - User Manual Verification 'Phase 3: Screen Integration & Compose Previews' (Protocol in workflow.md)

## Phase 4: Verification & Track Checkpoint
- [x] Task: Automated Project Verification
    - [x] Run `./gradlew testDebugUnitTest` and ensure 100% passing tests
    - [x] Run `./gradlew assembleDebug` to guarantee clean compilation without lint or build errors
- [x] Task: Conductor - User Manual Verification 'Phase 4: Verification & Track Checkpoint' (Protocol in workflow.md)
