package com.danilo.conductorexample.ui.addgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.conductorexample.data.remote.datasource.IgdbRemoteDataSource
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameSearchResult
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.domain.repository.PlatformRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(FlowPreview::class)
class AddGameViewModel(
    private val gameRepository: GameRepository,
    private val platformRepository: PlatformRepository,
    private val igdbRemoteDataSource: IgdbRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddGameUiState(isLoading = true))
    val uiState: StateFlow<AddGameUiState> = _uiState.asStateFlow()

    private val searchTitleFlow = MutableStateFlow("")

    init {
        loadPlatforms()
        observeSearchFlow()
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

    private fun observeSearchFlow() {
        viewModelScope.launch {
            searchTitleFlow
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val trimmed = query.trim()
                    if (trimmed.length >= 3) {
                        _uiState.update { it.copy(isSearchingIgdb = true) }
                        val result = igdbRemoteDataSource.searchGames(trimmed, limit = 8)
                        result.fold(
                            onSuccess = { results ->
                                _uiState.update {
                                    it.copy(
                                        isSearchingIgdb = false,
                                        igdbSuggestions = results,
                                        showSuggestions = results.isNotEmpty()
                                    )
                                }
                            },
                            onFailure = {
                                _uiState.update {
                                    it.copy(
                                        isSearchingIgdb = false,
                                        igdbSuggestions = emptyList(),
                                        showSuggestions = false
                                    )
                                }
                            }
                        )
                    } else {
                        _uiState.update {
                            it.copy(
                                isSearchingIgdb = false,
                                igdbSuggestions = emptyList(),
                                showSuggestions = false
                            )
                        }
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
        searchTitleFlow.value = newTitle
    }

    fun onSuggestionSelected(suggestion: GameSearchResult) {
        _uiState.update { current ->
            val matchedPlatforms = current.availablePlatforms
                .filter { avail ->
                    suggestion.platformNames.any { igdbPlat ->
                        igdbPlat.contains(avail.name, ignoreCase = true) ||
                                avail.name.contains(igdbPlat, ignoreCase = true)
                    }
                }
                .map { it.name }
                .toSet()

            current.copy(
                title = suggestion.title,
                overview = if (suggestion.overview.isNotBlank()) suggestion.overview else current.overview,
                coverImageUrl = suggestion.coverImageUrl,
                selectedPlatforms = if (matchedPlatforms.isNotEmpty()) matchedPlatforms else current.selectedPlatforms,
                showSuggestions = false,
                igdbSuggestions = emptyList()
            )
        }
    }

    fun onDismissSuggestions() {
        _uiState.update { it.copy(showSuggestions = false) }
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
                coverImageUrl = current.coverImageUrl,
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
