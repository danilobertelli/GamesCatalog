# Implementation Plan: Pre-populated Platforms & Add Game Screen

## Phase 1: Platform Domain, Storage & Pre-population (Room & TDD)
- [x] Task: Create Platform domain model and PlatformEntity in Room
- [x] Task: Create PlatformDao and pre-seed list of gaming platforms
- [x] Task: Update GamesCatalogDatabase (version 2) with Room pre-population callback
- [x] Task: Write failing unit tests in PlatformDaoTest and PlatformRepositoryTest [RED]
- [x] Task: Implement PlatformRepositoryImpl and register in Koin modules [GREEN]
- [x] Task: Conductor - User Manual Verification 'Phase 1 - Platforms Storage' (Protocol in workflow.md)

## Phase 2: Navigation Setup & Dependencies
- [x] Task: Add androidx-navigation-compose to libs.versions.toml and app/build.gradle.kts
- [x] Task: Define AppDestinations navigation routes
- [x] Task: Setup NavHost in MainActivity connecting GamesCatalogScreen
- [x] Task: Conductor - User Manual Verification 'Phase 2 - Navigation Setup' (Protocol in workflow.md)

## Phase 3: AddGameViewModel & State (TDD)
- [x] Task: Write failing unit tests in AddGameViewModelTest (loading platforms, validation, save) [RED]
- [x] Task: Implement AddGameUiState and AddGameViewModel [GREEN]
- [x] Task: Register AddGameViewModel into Koin appModule
- [x] Task: Conductor - User Manual Verification 'Phase 3 - ViewModel & State' (Protocol in workflow.md)

## Phase 4: Form UI Components & AddGameScreen (Compose)
- [x] Task: Extract all form strings, platform labels, and errors to res/values/strings.xml
- [x] Task: Implement form components (StarRatingPicker, StatusChipGroup, PlatformChipGroup) with Previews
- [x] Task: Assemble AddGameScreen with Scaffold, TopAppBar, scrollable form, and imePadding
- [x] Task: Connect Save action to navigateBack + Toast feedback
- [x] Task: Connect GamesCatalogScreen FAB to navigate to AddGameScreen
- [x] Task: Conductor - User Manual Verification 'Phase 4 - UI & Integration' (Protocol in workflow.md)
