package com.danilo.conductorexample.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.ui.navigation.AppDestination
import com.danilo.conductorexample.util.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GameDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeGameRepository = FakeGameRepository()

    private fun createViewModel(gameId: String = "game-1"): GameDetailViewModel {
        val savedStateHandle = SavedStateHandle(mapOf(AppDestination.Detail.ARG_GAME_ID to gameId))
        return GameDetailViewModel(
            savedStateHandle = savedStateHandle,
            gameRepository = fakeGameRepository
        )
    }

    @Test
    fun `initial state loads game details by gameId`() = runTest {
        val game = Game(
            id = "game-1",
            title = "The Witcher 3: Wild Hunt",
            platforms = listOf("PC (Windows)", "PlayStation 5"),
            status = GameStatus.PLAYING,
            rating = 4,
            overview = "Geralt searching for Ciri"
        )
        fakeGameRepository.upsertGame(game)

        val viewModel = createViewModel("game-1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isGameNotFound)
            assertEquals("The Witcher 3: Wild Hunt", state.game?.title)
            assertEquals(GameStatus.PLAYING, state.selectedStatus)
            assertEquals(4, state.rating)
            assertEquals("Geralt searching for Ciri", state.overview)
            assertFalse(state.isModified)
        }
    }

    @Test
    fun `initial state sets isGameNotFound when game does not exist`() = runTest {
        val viewModel = createViewModel("non-existent-game")

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isGameNotFound)
            assertNull(state.game)
        }
    }

    @Test
    fun `editing fields updates uiState and isModified status`() = runTest {
        val game = Game(
            id = "game-1",
            title = "Bloodborne",
            status = GameStatus.WANT_TO_PLAY,
            rating = null,
            overview = ""
        )
        fakeGameRepository.upsertGame(game)

        val viewModel = createViewModel("game-1")

        viewModel.onStatusChanged(GameStatus.COMPLETED)
        viewModel.onRatingChanged(5)
        viewModel.onOverviewChanged("A masterpiece.")

        val state = viewModel.uiState.value
        assertEquals(GameStatus.COMPLETED, state.selectedStatus)
        assertEquals(5, state.rating)
        assertEquals("A masterpiece.", state.overview)
        assertTrue(state.isModified)
    }

    @Test
    fun `save updates game in repository with edited fields and marks isSaved`() = runTest {
        val game = Game(
            id = "game-1",
            title = "Bloodborne",
            platforms = listOf("PlayStation 4"),
            status = GameStatus.WANT_TO_PLAY,
            rating = null,
            overview = "Initial notes"
        )
        fakeGameRepository.upsertGame(game)

        val viewModel = createViewModel("game-1")

        viewModel.onStatusChanged(GameStatus.COMPLETED)
        viewModel.onRatingChanged(5)
        viewModel.onOverviewChanged("Beaten after 40 hours!")
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertTrue(state.isSaved)

        val updatedGame = fakeGameRepository.savedGames.find { it.id == "game-1" }
        assertNotNull(updatedGame)
        assertEquals("Bloodborne", updatedGame?.title)
        assertEquals(listOf("PlayStation 4"), updatedGame?.platforms)
        assertEquals(GameStatus.COMPLETED, updatedGame?.status)
        assertEquals(5, updatedGame?.rating)
        assertEquals("Beaten after 40 hours!", updatedGame?.overview)
    }

    @Test
    fun `delete confirmation dialog can be shown and dismissed`() = runTest {
        val viewModel = createViewModel("game-1")

        assertFalse(viewModel.uiState.value.showDeleteConfirmation)

        viewModel.onDeleteClicked()
        assertTrue(viewModel.uiState.value.showDeleteConfirmation)

        viewModel.onDismissDeleteDialog()
        assertFalse(viewModel.uiState.value.showDeleteConfirmation)
    }

    @Test
    fun `confirm delete removes game from repository and marks isDeleted`() = runTest {
        val game = Game(
            id = "game-1",
            title = "Elden Ring",
            status = GameStatus.PLAYING
        )
        fakeGameRepository.upsertGame(game)

        val viewModel = createViewModel("game-1")

        viewModel.onConfirmDelete()

        val state = viewModel.uiState.value
        assertTrue(state.isDeleted)
        assertFalse(state.showDeleteConfirmation)
        assertTrue(fakeGameRepository.savedGames.isEmpty())
    }
}

private class FakeGameRepository : GameRepository {
    val savedGames = mutableListOf<Game>()
    private val gamesFlow = MutableStateFlow<List<Game>>(emptyList())

    override fun getAllGames(): Flow<List<Game>> = gamesFlow

    override fun getGamesByStatus(status: GameStatus): Flow<List<Game>> {
        return gamesFlow.map { list -> list.filter { it.status == status } }
    }

    override fun getGameById(id: String): Flow<Game?> {
        return gamesFlow.map { list -> list.find { it.id == id } }
    }

    override suspend fun upsertGame(game: Game) {
        savedGames.removeAll { it.id == game.id }
        savedGames.add(game)
        val current = gamesFlow.value.toMutableList()
        current.removeAll { it.id == game.id }
        current.add(game)
        gamesFlow.value = current
    }

    override suspend fun deleteGame(id: String) {
        savedGames.removeAll { it.id == id }
        val current = gamesFlow.value.toMutableList()
        current.removeAll { it.id == id }
        gamesFlow.value = current
    }
}
