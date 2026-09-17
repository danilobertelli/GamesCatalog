package com.danilo.conductorexample.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.conductorexample.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GamesCatalogViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")

    val uiState: StateFlow<GamesCatalogUiState> = combine(
        gameRepository.getAllGames(),
        searchQueryFlow
    ) { allGames, query ->
        val sortedAllGames = allGames.sortedBy { it.title.lowercase() }
        val filteredGames = if (query.isBlank()) {
            sortedAllGames
        } else {
            sortedAllGames.filter { it.title.contains(query, ignoreCase = true) }
        }

        GamesCatalogUiState(
            isLoading = false,
            games = filteredGames,
            searchQuery = query,
            isCatalogEmpty = sortedAllGames.isEmpty(),
            isSearchEmpty = sortedAllGames.isNotEmpty() && filteredGames.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GamesCatalogUiState(isLoading = true)
    )

    fun onSearchQueryChanged(newQuery: String) {
        searchQueryFlow.value = newQuery
    }
}
