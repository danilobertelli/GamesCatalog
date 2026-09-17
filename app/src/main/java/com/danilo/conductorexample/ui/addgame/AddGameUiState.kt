package com.danilo.conductorexample.ui.addgame

import com.danilo.conductorexample.domain.model.GameSearchResult
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.model.Platform

data class AddGameUiState(
    val title: String = "",
    val titleError: String? = null,
    val coverImageUrl: String? = null,
    val selectedStatus: GameStatus = GameStatus.WANT_TO_PLAY,
    val rating: Int? = null,
    val availablePlatforms: List<Platform> = emptyList(),
    val selectedPlatforms: Set<String> = emptySet(),
    val overview: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isSearchingIgdb: Boolean = false,
    val igdbSuggestions: List<GameSearchResult> = emptyList(),
    val showSuggestions: Boolean = false
)
