package com.danilo.conductorexample.ui.addgame

import app.cash.turbine.test
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.model.Platform
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.domain.repository.PlatformRepository
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

class AddGameViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeGameRepository = FakeGameRepository()
    private val fakePlatformRepository = FakePlatformRepository()

    private fun createViewModel(): AddGameViewModel {
        return AddGameViewModel(
            gameRepository = fakeGameRepository,
            platformRepository = fakePlatformRepository
        )
    }

    @Test
    fun `initial state loads available platforms and seeds if needed`() = runTest {
        val platforms = listOf(
            Platform("ps5", "PlayStation 5"),
            Platform("switch", "Nintendo Switch")
        )
        fakePlatformRepository.emitPlatforms(platforms)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.availablePlatforms.size)
            assertEquals("PlayStation 5", state.availablePlatforms[0].name)
            assertEquals(GameStatus.WANT_TO_PLAY, state.selectedStatus)
            assertNull(state.rating)
            assertTrue(state.selectedPlatforms.isEmpty())
            assertTrue(fakePlatformRepository.wasSeedCalled)
        }
    }

    @Test
    fun `title change updates title and clears title error`() = runTest {
        val viewModel = createViewModel()

        viewModel.onTitleChanged("Dark Souls")

        assertEquals("Dark Souls", viewModel.uiState.value.title)
        assertNull(viewModel.uiState.value.titleError)
    }

    @Test
    fun `status and rating updates reflect in uiState`() = runTest {
        val viewModel = createViewModel()

        viewModel.onStatusChanged(GameStatus.PLAYING)
        viewModel.onRatingChanged(4)

        assertEquals(GameStatus.PLAYING, viewModel.uiState.value.selectedStatus)
        assertEquals(4, viewModel.uiState.value.rating)
    }

    @Test
    fun `platform toggling adds and removes from selected platforms`() = runTest {
        val viewModel = createViewModel()

        viewModel.onPlatformToggled("PlayStation 5")
        assertTrue(viewModel.uiState.value.selectedPlatforms.contains("PlayStation 5"))

        viewModel.onPlatformToggled("Nintendo Switch")
        assertEquals(2, viewModel.uiState.value.selectedPlatforms.size)

        viewModel.onPlatformToggled("PlayStation 5")
        assertFalse(viewModel.uiState.value.selectedPlatforms.contains("PlayStation 5"))
        assertTrue(viewModel.uiState.value.selectedPlatforms.contains("Nintendo Switch"))
    }

    @Test
    fun `save with blank title sets title error and does not save`() = runTest {
        val viewModel = createViewModel()

        viewModel.onTitleChanged("   ")
        viewModel.onSaveClicked()

        assertNotNull(viewModel.uiState.value.titleError)
        assertFalse(viewModel.uiState.value.isSaved)
        assertTrue(fakeGameRepository.savedGames.isEmpty())
    }

    @Test
    fun `save with valid title saves game to repository and marks isSaved`() = runTest {
        val viewModel = createViewModel()

        viewModel.onTitleChanged("Bloodborne")
        viewModel.onStatusChanged(GameStatus.COMPLETED)
        viewModel.onRatingChanged(5)
        viewModel.onPlatformToggled("PlayStation 4")
        viewModel.onOverviewChanged("Masterpiece by FromSoftware")

        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertTrue(state.isSaved)
        assertNull(state.titleError)

        assertEquals(1, fakeGameRepository.savedGames.size)
        val saved = fakeGameRepository.savedGames[0]
        assertEquals("Bloodborne", saved.title)
        assertEquals(GameStatus.COMPLETED, saved.status)
        assertEquals(5, saved.rating)
        assertTrue(saved.platforms.contains("PlayStation 4"))
        assertEquals("Masterpiece by FromSoftware", saved.overview)
    }
}

private class FakePlatformRepository : PlatformRepository {
    private val flow = MutableStateFlow<List<Platform>>(emptyList())
    var wasSeedCalled = false

    fun emitPlatforms(platforms: List<Platform>) {
        flow.value = platforms
    }

    override fun getAllPlatforms(): Flow<List<Platform>> = flow

    override suspend fun seedPlatformsIfNeeded() {
        wasSeedCalled = true
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
        savedGames.add(game)
        val current = gamesFlow.value.toMutableList()
        current.add(game)
        gamesFlow.value = current
    }

    override suspend fun deleteGame(id: String) {
        savedGames.removeAll { it.id == id }
    }
}
