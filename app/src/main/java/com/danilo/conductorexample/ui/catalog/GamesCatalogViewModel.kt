package com.danilo.conductorexample.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel managing the state and business logic for the main Games Catalog screen.
 *
 * Combines the reactive game list stream from [GameRepository] with the current user
 * search query and selected game status filter to produce an immutable, filtered [GamesCatalogUiState].
 *
 * @param gameRepository Repository for querying user game entries.
 */
class GamesCatalogViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    private val selectedStatusFlow = MutableStateFlow<GameStatus?>(null)

    val uiState: StateFlow<GamesCatalogUiState> = combine(
        gameRepository.getAllGames(),
        searchQueryFlow,
        selectedStatusFlow
    ) { allGames, query, selectedStatus ->
        val sortedAllGames = allGames.sortedBy { it.title.lowercase() }
        val filteredGames = sortedAllGames.filter { game ->
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                game.title.contains(query, ignoreCase = true)
            }
            val matchesStatus = selectedStatus == null || game.status == selectedStatus
            matchesQuery && matchesStatus
        }

        GamesCatalogUiState(
            isLoading = false,
            games = filteredGames,
            searchQuery = query,
            selectedStatus = selectedStatus,
            isCatalogEmpty = sortedAllGames.isEmpty(),
            isSearchEmpty = sortedAllGames.isNotEmpty() && filteredGames.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GamesCatalogUiState(isLoading = true)
    )

    /**
     * Updates the current search query filter.
     *
     * @param newQuery The text query to filter games by title.
     */
    fun onSearchQueryChanged(newQuery: String) {
        searchQueryFlow.value = newQuery
    }

    /**
     * Updates the active status filter. Passing `null` removes the filter (displaying all statuses).
     *
     * @param status The [GameStatus] to filter by, or `null` for all games.
     */
    fun onStatusFilterSelected(status: GameStatus?) {
        selectedStatusFlow.value = status
    }
}
