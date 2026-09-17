# Implementation Plan: Pre-populated Platforms & Add Game Screen

## Phase 1: Platform Domain, Storage & Pre-population (Room & TDD)
- [ ] Task: Create Platform domain model and PlatformEntity in Room
- [ ] Task: Create PlatformDao and pre-seed list of gaming platforms
- [ ] Task: Update GamesCatalogDatabase (version 2) with Room pre-population callback
- [ ] Task: Write failing unit tests in PlatformDaoTest and PlatformRepositoryTest [RED]
- [ ] Task: Implement PlatformRepositoryImpl and register in Koin modules [GREEN]
- [ ] Task: Conductor - User Manual Verification 'Phase 1 - Platforms Storage' (Protocol in workflow.md)

## Phase 2: Navigation Setup & Dependencies
- [ ] Task: Add androidx-navigation-compose to libs.versions.toml and app/build.gradle.kts
- [ ] Task: Define AppDestinations navigation routes
- [ ] Task: Setup NavHost in MainActivity connecting GamesCatalogScreen
- [ ] Task: Conductor - User Manual Verification 'Phase 2 - Navigation Setup' (Protocol in workflow.md)

## Phase 3: AddGameViewModel & State (TDD)
- [ ] Task: Write failing unit tests in AddGameViewModelTest (loading platforms, validation, save) [RED]
- [ ] Task: Implement AddGameUiState and AddGameViewModel [GREEN]
- [ ] Task: Register AddGameViewModel into Koin appModule
- [ ] Task: Conductor - User Manual Verification 'Phase 3 - ViewModel & State' (Protocol in workflow.md)

## Phase 4: Form UI Components & AddGameScreen (Compose)
- [ ] Task: Extract all form strings, platform labels, and errors to res/values/strings.xml
- [ ] Task: Implement form components (StarRatingPicker, StatusChipGroup, PlatformChipGroup) with Previews
- [ ] Task: Assemble AddGameScreen with Scaffold, TopAppBar, scrollable form, and imePadding
- [ ] Task: Connect Save action to navigateBack + Toast feedback
- [ ] Task: Connect GamesCatalogScreen FAB to navigate to AddGameScreen
- [ ] Task: Conductor - User Manual Verification 'Phase 4 - UI & Integration' (Protocol in workflow.md)
