# Specification: Game Detail and Edit Screen with Delete Confirmation

## Overview
This feature introduces a dedicated Game Detail and Edit screen (`GameDetailScreen`) in the GamesCatalog app. When a user taps on any game item in the main catalog list, the app navigates to this screen with the corresponding game ID. Users can review all existing information about the game, update their progress/status, rating, and notes/overview, or completely delete the game from their local catalog with a confirmation dialog.

## Functional Requirements
1. **Catalog Item Tap & Navigation:**
   - Tapping a game card in `GamesCatalogScreen` navigates to `GameDetailScreen` using the route `game_detail/{gameId}`.
   - The screen retrieves the game data from `GameRepository.getGameById(gameId)`.

2. **Read-only Information:**
   - **Game Title:** Displayed prominently as read-only.
   - **Platforms:** Displayed as read-only badges/chips showing which platforms the game was saved with.

3. **Editable Fields:**
   - **Status:** Interactive `StatusChipGroup` allowing the user to change between `Want to Play`, `Playing`, `Completed`, and `Abandoned`.
   - **Rating:** Interactive `StarRatingPicker` (1-5 stars with ability to clear).
   - **Notes / Overview:** `OutlinedTextField` allowing the user to update notes/overview.

4. **Explicit Save Action:**
   - A sticky "Save Changes" button in the bottom bar triggers updating the game in Room via `GameRepository.upsertGame`.
   - Displays a confirmation Toast message (`Game updated successfully`) and navigates back to the catalog.

5. **Delete Action & Confirmation Dialog:**
   - An accessible delete action (e.g. Trash icon in the `TopAppBar`) prompts an `AlertDialog`.
   - The dialog displays a clear title and warning message: "Are you sure you want to delete this game? This action cannot be undone." with "Delete" and "Cancel" buttons.
   - Confirming delete invokes `GameRepository.deleteGame(id)`, displays a Toast message (`Game deleted`), and navigates back to the catalog.
   - Canceling dismisses the dialog without deleting.

6. **Edge-to-Edge & Platform Standards:**
   - TopAppBar respects status bar insets (`statusBarsPadding()`).
   - Bottom bar respects navigation bar insets (`navigationBarsPadding()`).
   - Form content handles keyboard visibility with `imePadding()` and vertical scrolling.
   - 100% of strings, error messages, dialog texts, and content descriptions must be localized in `res/values/strings.xml`.

## Non-Functional Requirements
- **Architecture:** Unidirectional Data Flow (UDF) with `GameDetailViewModel` and `GameDetailUiState`.
- **Dependency Injection:** ViewModels injected cleanly via Koin (`koinViewModel()`).
- **Testing:** Unit tests for `GameDetailViewModel` covering load, state changes, save, and delete.

## Acceptance Criteria
- [x] Tapping a game card in the catalog navigates to `game_detail/{gameId}`.
- [x] Title and platforms are read-only.
- [x] Status, rating, and notes can be edited.
- [x] Tapping "Save Changes" persists updates to Room and returns to catalog with a success Toast.
- [x] Tapping the delete icon displays a confirmation dialog.
- [x] Confirming deletion removes the game from Room and returns to catalog with a Toast.
- [x] Unit tests pass with 100% success.
- [x] No hardcoded strings in code.
