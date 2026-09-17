package com.danilo.conductorexample.ui.addgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.domain.repository.PlatformRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class AddGameViewModel(
    private val gameRepository: GameRepository,
    private val platformRepository: PlatformRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddGameUiState(isLoading = true))
    val uiState: StateFlow<AddGameUiState> = _uiState.asStateFlow()

    init {
        loadPlatforms()
    }

    private fun loadPlatforms() {
        viewModelScope.launch {
            platformRepository.seedPlatformsIfNeeded()
            platformRepository.getAllPlatforms().collect { platforms ->
                _uiState.update { current ->
                    current.copy(
                        availablePlatforms = platforms,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onTitleChanged(newTitle: String) {
        _uiState.update {
            it.copy(
                title = newTitle,
                titleError = null
            )
        }
    }

    fun onStatusChanged(newStatus: GameStatus) {
        _uiState.update { it.copy(selectedStatus = newStatus) }
    }

    fun onRatingChanged(newRating: Int?) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun onPlatformToggled(platformName: String) {
        _uiState.update { current ->
            val updated = current.selectedPlatforms.toMutableSet()
            if (updated.contains(platformName)) {
                updated.remove(platformName)
            } else {
                updated.add(platformName)
            }
            current.copy(selectedPlatforms = updated)
        }
    }

    fun onOverviewChanged(newOverview: String) {
        _uiState.update { it.copy(overview = newOverview) }
    }

    fun onSaveClicked() {
        val current = _uiState.value
        if (current.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Title cannot be blank") }
            return
        }

        viewModelScope.launch {
            val newGame = Game(
                id = UUID.randomUUID().toString(),
                title = current.title.trim(),
                overview = current.overview.trim(),
                coverImageUrl = null,
                platforms = current.selectedPlatforms.toList(),
                status = current.selectedStatus,
                rating = current.rating,
                completionDateEpochMs = if (current.selectedStatus == GameStatus.COMPLETED) {
                    System.currentTimeMillis()
                } else null
            )
            gameRepository.upsertGame(newGame)
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
