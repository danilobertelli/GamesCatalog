# Implementation Plan: Setup Room Local Storage, Domain Models, Repository, and Koin DI

## Phase 1: Dependency Setup (Room, KSP, Koin)
- [ ] Task: Configure Gradle plugins and dependencies for KSP, Room, and Koin
    - [ ] Add KSP plugin to `gradle/libs.versions.toml` and root `build.gradle.kts`
    - [ ] Add Room runtime, ktx, and compiler (via ksp) to version catalog and `app/build.gradle.kts`
    - [ ] Add Koin Android and Koin Compose dependencies to version catalog and `app/build.gradle.kts`
    - [ ] Sync and compile project to ensure dependencies resolve
- [ ] Task: Conductor - User Manual Verification 'Phase 1: Dependency Setup' (Protocol in workflow.md)

## Phase 2: Domain Layer Definition
- [ ] Task: Create Domain Models and Enums
    - [ ] Write unit test for `Game` and `GameStatus` validation rules
    - [ ] Implement `GameStatus` enum (`WANT_TO_PLAY`, `PLAYING`, `COMPLETED`, `ABANDONED`)
    - [ ] Implement `Game` immutable data class
    - [ ] Define `GameRepository` domain interface
- [ ] Task: Conductor - User Manual Verification 'Phase 2: Domain Layer Definition' (Protocol in workflow.md)

## Phase 3: Room Persistence Layer (TDD)
- [ ] Task: Create Room Entity, Type Converters, and DAO
    - [ ] Write failing unit test for `GameDao` using in-memory Room database
    - [ ] Implement `GameEntity` and converters for platforms and status
    - [ ] Implement `GameDao` with suspended CRUD and Flow queries
    - [ ] Create `GamesCatalogDatabase` class
    - [ ] Run test and verify all DAO tests pass (Green phase)
- [ ] Task: Implement GameRepository with Domain Mapping (TDD)
    - [ ] Write failing unit test for `GameRepositoryImpl`
    - [ ] Implement `GameRepositoryImpl` mapping entities to domain models
    - [ ] Run test and verify repository tests pass (Green phase)
- [ ] Task: Conductor - User Manual Verification 'Phase 3: Room Persistence Layer' (Protocol in workflow.md)

## Phase 4: Koin Dependency Injection & Application Wiring
- [ ] Task: Configure Koin Modules and Application Class
    - [ ] Write unit test to verify Koin module configuration (`verify()` or checkModules)
    - [ ] Create `databaseModule` providing database and DAO
    - [ ] Create `repositoryModule` providing `GameRepository`
    - [ ] Implement `GamesCatalogApplication` inheriting `Application` and configuring `startKoin`
    - [ ] Register `GamesCatalogApplication` in `AndroidManifest.xml`
- [ ] Task: Conductor - User Manual Verification 'Phase 4: Koin Dependency Injection' (Protocol in workflow.md)

## Phase 5: Verification & Checkpoint
- [ ] Task: Automated Project Verification
    - [ ] Run `./gradlew testDebugUnitTest` and ensure 100% pass rate
    - [ ] Confirm adherence to Kotlin & Android style guides
- [ ] Task: Conductor - User Manual Verification 'Phase 5: Verification & Checkpoint' (Protocol in workflow.md)
