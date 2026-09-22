package com.danilo.conductorexample.ui.catalog

import app.cash.turbine.test
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.util.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GamesCatalogViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepository = FakeGameRepository()

    private fun createViewModel(): GamesCatalogViewModel {
        return GamesCatalogViewModel(gameRepository = fakeRepository)
    }

    @Test
    fun `catalog sorts games alphabetically A to Z`() = runTest {
        val gameZ = Game(id = "1", title = "Zelda: Tears of the Kingdom", status = GameStatus.PLAYING, rating = 5)
        val gameA = Game(id = "2", title = "Alan Wake 2", status = GameStatus.COMPLETED, rating = 4)
        val gameM = Game(id = "3", title = "Metroid Prime", status = GameStatus.WANT_TO_PLAY, rating = null)

        fakeRepository.emitGames(listOf(gameZ, gameA, gameM))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isCatalogEmpty)
            assertEquals(3, state.games.size)
            assertEquals("Alan Wake 2", state.games[0].title)
            assertEquals("Metroid Prime", state.games[1].title)
            assertEquals("Zelda: Tears of the Kingdom", state.games[2].title)
        }
    }

    @Test
    fun `search query filters games matching title case-insensitively`() = runTest {
        val game1 = Game(id = "1", title = "Super Mario Odyssey", status = GameStatus.COMPLETED)
        val game2 = Game(id = "2", title = "Super Metroid", status = GameStatus.COMPLETED)
        val game3 = Game(id = "3", title = "The Witcher 3", status = GameStatus.PLAYING)

        fakeRepository.emitGames(listOf(game1, game2, game3))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(3, initialState.games.size)

            viewModel.onSearchQueryChanged("super")

            val filteredState = awaitItem()
            assertEquals("super", filteredState.searchQuery)
            assertEquals(2, filteredState.games.size)
            assertEquals("Super Mario Odyssey", filteredState.games[0].title)
            assertEquals("Super Metroid", filteredState.games[1].title)
            assertFalse(filteredState.isSearchEmpty)
        }
    }

    @Test
    fun `empty catalog sets isCatalogEmpty to true`() = runTest {
        fakeRepository.emitGames(emptyList())
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isCatalogEmpty)
            assertFalse(state.isSearchEmpty)
            assertTrue(state.games.isEmpty())
        }
    }

    @Test
    fun `search query with no matches sets isSearchEmpty to true`() = runTest {
        val game = Game(id = "1", title = "God of War", status = GameStatus.COMPLETED)
        fakeRepository.emitGames(listOf(game))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial state

            viewModel.onSearchQueryChanged("Halo")

            val searchState = awaitItem()
            assertEquals("Halo", searchState.searchQuery)
            assertTrue(searchState.games.isEmpty())
            assertTrue(searchState.isSearchEmpty)
            assertFalse(searchState.isCatalogEmpty)
        }
    }

    @Test
    fun `initial state has null selectedStatus`() = runTest {
        val game = Game(id = "1", title = "Chrono Trigger", status = GameStatus.COMPLETED)
        fakeRepository.emitGames(listOf(game))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.selectedStatus)
            assertEquals(1, state.games.size)
        }
    }

    @Test
    fun `filter by status filters games matching selected status`() = runTest {
        val game1 = Game(id = "1", title = "Alan Wake 2", status = GameStatus.COMPLETED)
        val game2 = Game(id = "2", title = "Metroid Prime", status = GameStatus.PLAYING)
        val game3 = Game(id = "3", title = "Hollow Knight", status = GameStatus.WANT_TO_PLAY)
        val game4 = Game(id = "4", title = "Dark Souls 2", status = GameStatus.ABANDONED)

        fakeRepository.emitGames(listOf(game1, game2, game3, game4))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(4, initial.games.size)
            assertNull(initial.selectedStatus)

            viewModel.onStatusFilterSelected(GameStatus.PLAYING)

            val playingState = awaitItem()
            assertEquals(GameStatus.PLAYING, playingState.selectedStatus)
            assertEquals(1, playingState.games.size)
            assertEquals("Metroid Prime", playingState.games[0].title)

            viewModel.onStatusFilterSelected(GameStatus.COMPLETED)

            val completedState = awaitItem()
            assertEquals(GameStatus.COMPLETED, completedState.selectedStatus)
            assertEquals(1, completedState.games.size)
            assertEquals("Alan Wake 2", completedState.games[0].title)

            viewModel.onStatusFilterSelected(null)

            val allState = awaitItem()
            assertNull(allState.selectedStatus)
            assertEquals(4, allState.games.size)
        }
    }

    @Test
    fun `filter by status combined with search query matches both criteria`() = runTest {
        val game1 = Game(id = "1", title = "Super Mario Odyssey", status = GameStatus.COMPLETED)
        val game2 = Game(id = "2", title = "Super Mario Wonder", status = GameStatus.PLAYING)
        val game3 = Game(id = "3", title = "Super Metroid", status = GameStatus.COMPLETED)

        fakeRepository.emitGames(listOf(game1, game2, game3))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.onSearchQueryChanged("Mario")
            val searchState = awaitItem()
            assertEquals(2, searchState.games.size)

            viewModel.onStatusFilterSelected(GameStatus.COMPLETED)
            val combinedState = awaitItem()
            assertEquals(1, combinedState.games.size)
            assertEquals("Super Mario Odyssey", combinedState.games[0].title)
            assertEquals(GameStatus.COMPLETED, combinedState.selectedStatus)
        }
    }

    @Test
    fun `status filter with no matches sets isSearchEmpty to true`() = runTest {
        val game = Game(id = "1", title = "Elden Ring", status = GameStatus.COMPLETED)
        fakeRepository.emitGames(listOf(game))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.onStatusFilterSelected(GameStatus.ABANDONED)
            val filteredState = awaitItem()
            assertEquals(GameStatus.ABANDONED, filteredState.selectedStatus)
            assertTrue(filteredState.games.isEmpty())
            assertTrue(filteredState.isSearchEmpty)
            assertFalse(filteredState.isCatalogEmpty)
        }
    }
}

private class FakeGameRepository : GameRepository {
    private val gamesFlow = MutableStateFlow<List<Game>>(emptyList())

    fun emitGames(games: List<Game>) {
        gamesFlow.value = games
    }

    override fun getAllGames(): Flow<List<Game>> = gamesFlow

    override fun getGamesByStatus(status: GameStatus): Flow<List<Game>> {
        return gamesFlow.map { list -> list.filter { it.status == status } }
    }

    override fun getGameById(id: String): Flow<Game?> {
        return gamesFlow.map { list -> list.find { it.id == id } }
    }

    override suspend fun upsertGame(game: Game) {
        val current = gamesFlow.value.toMutableList()
        current.removeAll { it.id == game.id }
        current.add(game)
        gamesFlow.value = current
    }

    override suspend fun deleteGame(id: String) {
        val current = gamesFlow.value.toMutableList()
        current.removeAll { it.id == id }
        gamesFlow.value = current
    }
}
