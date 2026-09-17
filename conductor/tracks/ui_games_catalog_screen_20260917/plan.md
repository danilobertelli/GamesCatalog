# Implementation Plan: GamesCatalogScreen UI with Search, Filtered List, and FAB

## Phase 1: ViewModel & UI State (TDD)
- [x] Task: Define UI State and write ViewModel Unit Tests (Red Phase)
    - [x] Create `GamesCatalogUiState` sealed hierarchy / data class
    - [x] Write `GamesCatalogViewModelTest` covering initial load, alphabetical sorting, search query filtering, and empty states
    - [x] Run test and verify failure (Red Phase)
- [x] Task: Implement GamesCatalogViewModel and Koin Wiring (Green Phase)
    - [x] Implement `GamesCatalogViewModel` combining repository Flow with search query StateFlow
    - [x] Register `GamesCatalogViewModel` in Koin `AppModule.kt`
    - [x] Run tests and verify all ViewModel tests pass (Green Phase)
- [x] Task: Conductor - User Manual Verification 'Phase 1: ViewModel & UI State' (Protocol in workflow.md)

## Phase 2: UI Components & Design System
- [ ] Task: Create Atomic Composables
    - [ ] Implement `GameListItem` with cover placeholder Box, title, and rating ("x/5")
    - [ ] Implement `CatalogSearchBar` with Material 3 styling, search icon, and clear action
    - [ ] Implement `CatalogEmptyState` supporting both empty library and empty search results
- [ ] Task: Conductor - User Manual Verification 'Phase 2: UI Components & Design System' (Protocol in workflow.md)

## Phase 3: Screen Assembly & Scaffold Integration
- [ ] Task: Assemble GamesCatalogScreen
    - [ ] Create `GamesCatalogScreen` integrating Scaffold, TopBar/SearchBar, LazyColumn, and FAB
    - [ ] Wire `onAddGameClick` callback
    - [ ] Add Compose `@Preview` configurations (populated catalog, search filtering, empty states)
    - [ ] Connect `GamesCatalogScreen` in `MainActivity.kt`
- [ ] Task: Conductor - User Manual Verification 'Phase 3: Screen Assembly & Scaffold Integration' (Protocol in workflow.md)

## Phase 4: Verification & Track Checkpoint
- [ ] Task: Automated Project Verification
    - [ ] Run `./gradlew testDebugUnitTest` ensuring 100% pass rate
    - [ ] Run `./gradlew assembleDebug` to verify complete UI compilation
- [ ] Task: Conductor - User Manual Verification 'Phase 4: Verification & Track Checkpoint' (Protocol in workflow.md)
