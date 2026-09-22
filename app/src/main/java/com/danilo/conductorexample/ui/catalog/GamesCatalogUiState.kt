package com.danilo.conductorexample.ui.catalog

import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus

/**
 * UI State for the Games Catalog screen.
 *
 * @property isLoading Indicates whether the initial games data is being loaded.
 * @property games The current list of games to be displayed, filtered and sorted.
 * @property searchQuery The current active search query filtering games by title.
 * @property selectedStatus The current status filter applied, or null if all statuses are shown.
 * @property isCatalogEmpty True if the user's catalog is completely empty without any registered games.
 * @property isSearchEmpty True if games exist in the catalog but none match the current search query or status filter.
 */
data class GamesCatalogUiState(
    val isLoading: Boolean = true,
    val games: List<Game> = emptyList(),
    val searchQuery: String = "",
    val selectedStatus: GameStatus? = null,
    val isCatalogEmpty: Boolean = false,
    val isSearchEmpty: Boolean = false
)
