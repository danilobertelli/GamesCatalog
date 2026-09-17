package com.danilo.conductorexample.ui.detail

import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus

data class GameDetailUiState(
    val isLoading: Boolean = true,
    val isGameNotFound: Boolean = false,
    val game: Game? = null,
    val selectedStatus: GameStatus = GameStatus.WANT_TO_PLAY,
    val rating: Int? = null,
    val overview: String = "",
    val showDeleteConfirmation: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
) {
    val isModified: Boolean
        get() = game != null && (
            selectedStatus != game.status ||
            rating != game.rating ||
            overview != (game.overview ?: "")
        )
}
