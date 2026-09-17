# Kotlin & Android Architecture Style Guide

## 1. Language & Idiomatic Kotlin
- **Immutability First:** Use `val` by default; avoid `var` unless strictly required for mutable state machines.
- **Data Classes:** Use immutable `data class` with read-only properties for UI states, entities, and domain models.
- **Null Safety:** Avoid force unwrapping (`!!`). Use safe calls (`?.`), Elvis operator (`?:`), or structured smart casting.
- **Expressions:** Prefer idiomatic Kotlin constructs (`when`, `let`, `run`, `apply`) where readability is preserved.

## 2. Jetpack Compose Guidelines
- **State Hoisting:** Composables must not own business logic. Pass state down and propagate events/callbacks up.
- **Recomposition Safety:**
  - Mark immutable collections or use Kotlinx Immutable Collections if needed to avoid unnecessary recompositions.
  - Never run side-effects directly in a Composable body; always use `LaunchedEffect`, `rememberCoroutineScope`, or `DisposableEffect`.
- **Modifier Discipline:** Always provide a `modifier: Modifier = Modifier` parameter as the first optional parameter in reusable Composables.
- **Tokens & Theming:** Use `MaterialTheme.colorScheme` and `MaterialTheme.typography`; do not hardcode raw hex colors or fixed pixel sizes in Composables.

## 3. Architecture & Coroutines
- **Concurrency:**
  - Launch coroutines exclusively within structured lifecycles (`viewModelScope`). Never use `GlobalScope`.
  - Discard thread-blocking I/O operations from the main dispatcher. Ensure repository calls use `withContext(Dispatchers.IO)`.
- **Flows:**
  - Expose read-only `StateFlow` from ViewModels to Compose screens.
  - Convert Flows using `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialState)`.

## 4. Room Database Best Practices
- Entities must represent pure relational schema tables.
- Use DAOs with suspended functions for single operations and `Flow<T>` for observable data changes.
- Avoid performing database operations on the main thread.
