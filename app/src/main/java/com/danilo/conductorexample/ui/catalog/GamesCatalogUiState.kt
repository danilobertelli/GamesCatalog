package com.danilo.conductorexample.ui.catalog

import com.danilo.conductorexample.domain.model.Game

data class GamesCatalogUiState(
    val isLoading: Boolean = true,
    val games: List<Game> = emptyList(),
    val searchQuery: String = "",
    val isCatalogEmpty: Boolean = false,
    val isSearchEmpty: Boolean = false
)
