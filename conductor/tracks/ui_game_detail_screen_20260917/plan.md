# Implementation Plan: Game Detail & Edit Screen with Delete Confirmation

## Phase 1: Navigation Setup for Game Detail Route
- [x] Task: Define `AppDestination.Detail` with route `game_detail/{gameId}` and navigation argument in `AppDestinations.kt`
- [x] Task: Update `GamesCatalogScreen` to expose `onGameClick: (gameId: String) -> Unit` and wire `GameListItem` click
- [x] Task: Register `game_detail/{gameId}` destination in `AppNavHost.kt`
- [x] Task: Conductor - User Manual Verification 'Phase 1 - Navigation Setup' (Protocol in workflow.md)

## Phase 2: GameDetailViewModel & State Machine (TDD)
- [x] Task: Model `GameDetailUiState` (loading, gameNotFound, editable status, rating, overview, isSaved, isDeleted, showDeleteDialog)
- [x] Task: Write unit tests in `GameDetailViewModelTest.kt` verifying loading, editing fields, saving changes, and deleting game
- [x] Task: Implement `GameDetailViewModel` with `GameRepository` injection, `SavedStateHandle` for `gameId`, and state mutators
- [x] Task: Register `viewModelOf(::GameDetailViewModel)` in `AppModule.kt` and update `KoinModulesTest.kt`
- [x] Task: Run `./gradlew testDebugUnitTest` and verify all tests pass
- [x] Task: Conductor - User Manual Verification 'Phase 2 - ViewModel & State' (Protocol in workflow.md)

## Phase 3: Localized Strings & Delete Confirmation Dialog (Compose)
- [x] Task: Extract all strings to `res/values/strings.xml` (screen title, labels, save button, delete button, dialog title, dialog message, confirm/cancel, Toasts)
- [x] Task: Create `DeleteGameConfirmationDialog` composable with confirmation and cancellation actions + Preview
- [x] Task: Conductor - User Manual Verification 'Phase 3 - Strings & Dialog' (Protocol in workflow.md)

## Phase 4: GameDetailScreen UI Assembly & Build/Installation (Compose)
- [x] Task: Implement `GameDetailScreen` and `GameDetailContent` composables with Scaffold, TopAppBar (back + delete), read-only info, editable components, and sticky Save button
- [x] Task: Connect delete confirmation dialog and save/delete lifecycle events (Toast + popBackStack)
- [x] Task: Wire `GameDetailScreen` inside `AppNavHost.kt`
- [x] Task: Run `./gradlew compileDebugKotlin testDebugUnitTest assembleDebug`
- [x] Task: Install APK on connected physical device via `./gradlew installDebug` for developer verification
- [x] Task: Conductor - User Manual Verification 'Phase 4 - UI Assembly & Installation' (Protocol in workflow.md)
