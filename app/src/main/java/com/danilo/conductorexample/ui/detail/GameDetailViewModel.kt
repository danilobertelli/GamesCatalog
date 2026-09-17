package com.danilo.conductorexample.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.ui.navigation.AppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing the details view, edit interactions, and deletion of a game.
 *
 * Extracts the `gameId` from navigation [SavedStateHandle] arguments, retrieves the game
 * record from [GameRepository], coordinates rating and status edits, and executes deletions.
 *
 * @param savedStateHandle Navigation handle containing destination route arguments.
 * @param gameRepository Repository for querying, modifying, or deleting game entities.
 */
class GameDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val gameId: String = savedStateHandle.get<String>(AppDestination.Detail.ARG_GAME_ID).orEmpty()

    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            if (gameId.isBlank()) {
                _uiState.update { it.copy(isLoading = false, isGameNotFound = true) }
                return@launch
            }

            val game = gameRepository.getGameById(gameId).firstOrNull()
            if (game == null) {
                _uiState.update { it.copy(isLoading = false, isGameNotFound = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isGameNotFound = false,
                        game = game,
                        selectedStatus = game.status,
                        rating = game.rating,
                        overview = game.overview ?: ""
                    )
                }
            }
        }
    }

    fun onStatusChanged(status: GameStatus) {
        _uiState.update { it.copy(selectedStatus = status) }
    }

    fun onRatingChanged(rating: Int?) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun onOverviewChanged(overview: String) {
        _uiState.update { it.copy(overview = overview) }
    }

    fun onSaveClicked() {
        val currentGame = _uiState.value.game ?: return
        viewModelScope.launch {
            val updatedGame = currentGame.copy(
                status = _uiState.value.selectedStatus,
                rating = _uiState.value.rating,
                overview = _uiState.value.overview
            )
            gameRepository.upsertGame(updatedGame)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun onDeleteClicked() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun onConfirmDelete() {
        viewModelScope.launch {
            gameRepository.deleteGame(gameId)
            _uiState.update {
                it.copy(
                    showDeleteConfirmation = false,
                    isDeleted = true
                )
            }
        }
    }
}
