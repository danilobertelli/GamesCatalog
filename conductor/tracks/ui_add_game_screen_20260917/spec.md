# Specification: Add Game Screen & Pre-populated Platforms

## Overview
Implement the "Add Game" screen allowing users to register games locally, preceded by
a pre-populated Platform database table in Room to enforce standardized platform choices.

## Functional Requirements
1. Platforms Persistence & Pre-population:
   - Platform domain model (`Platform`) and Room entity (`PlatformEntity`).
   - Standard list of major platforms pre-seeded on database creation:
     - PlayStation: PS5, PS4, PS3, PS2, PS1
     - Xbox: Series X/S, Xbox One, Xbox 360, Original Xbox
     - Nintendo: Switch, Wii U, Wii, 3DS, DS, GBA, N64, SNES, NES
     - PC & Handhelds: PC (Windows), Mac, Linux, Steam Deck
     - Mobile: Android, iOS
   - `PlatformDao` and `PlatformRepository` exposing `getAllPlatforms(): Flow<List<Platform>>`.
2. Navigation:
   - Jetpack Navigation Compose with routes: `catalog` and `add_game`.
   - FAB on `GamesCatalogScreen` navigates to `add_game`.
   - Back button on TopBar and successful save navigate back to `catalog`.
3. Form Fields on AddGameScreen:
   - Title (Mandatory): Non-blank text validation.
   - Status (Mandatory): FilterChips for WANT_TO_PLAY, PLAYING, COMPLETED, ABANDONED.
   - Rating (Optional): 1 to 5 star rating selector.
   - Platforms (Optional / Standardized): Multi-selection chips fed directly from the pre-populated Room table.
   - Overview / Notes (Optional): Multiline text area.
4. Actions & Feedback:
   - Save button persists `Game` via `GameRepository.upsertGame()`.
   - Success Toast and popBackStack to catalog.
5. Insets & Strings:
   - Full Edge-to-Edge compliance (`statusBarsPadding()`, `imePadding()`).
   - All labels, platforms, titles, and error states extracted into `strings.xml`.

## Non-Functional Requirements
- minSdk 36, Material 3 theming.
- TDD Red-Green cycle for ViewModel and Repository logic.
- Koin dependency injection.
