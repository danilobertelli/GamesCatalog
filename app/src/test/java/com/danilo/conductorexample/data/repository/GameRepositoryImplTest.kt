package com.danilo.conductorexample.data.repository

import app.cash.turbine.test
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.entity.GameEntity
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GameRepositoryImplTest {

    private lateinit var fakeDao: FakeGameDao
    private lateinit var repository: GameRepositoryImpl

    @Before
    fun setup() {
        fakeDao = FakeGameDao()
        repository = GameRepositoryImpl(fakeDao)
    }

    @Test
    fun `getAllGames maps entity list to domain model list`() = runTest {
        val entity = GameEntity(
            id = "g-1",
            title = "Super Mario Odyssey",
            overview = "Platformer",
            coverImageUrl = "https://example.com/mario.jpg",
            platforms = listOf("Nintendo Switch"),
            status = "COMPLETED",
            rating = 5,
            completionDateEpochMs = 1600000000000L
        )
        fakeDao.upsertGame(entity)

        repository.getAllGames().test {
            val games = awaitItem()
            assertEquals(1, games.size)
            val game = games[0]
            assertEquals("g-1", game.id)
            assertEquals("Super Mario Odyssey", game.title)
            assertEquals(GameStatus.COMPLETED, game.status)
            assertEquals(5, game.rating)
            assertEquals(listOf("Nintendo Switch"), game.platforms)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGamesByStatus filters correctly`() = runTest {
        fakeDao.upsertGame(
            GameEntity(
                id = "1",
                title = "G1",
                overview = "",
                coverImageUrl = null,
                platforms = emptyList(),
                status = "PLAYING",
                rating = 4,
                completionDateEpochMs = null
            )
        )
        fakeDao.upsertGame(
            GameEntity(
                id = "2",
                title = "G2",
                overview = "",
                coverImageUrl = null,
                platforms = emptyList(),
                status = "WANT_TO_PLAY",
                rating = null,
                completionDateEpochMs = null
            )
        )

        repository.getGamesByStatus(GameStatus.PLAYING).test {
            val games = awaitItem()
            assertEquals(1, games.size)
            assertEquals("G1", games[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGameById returns null when not found`() = runTest {
        repository.getGameById("non-existent").test {
            val item = awaitItem()
            assertNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `upsertGame converts domain to entity and saves`() = runTest {
        val domainGame = Game(
            id = "g-new",
            title = "Metroid Prime",
            overview = "First-person adventure",
            coverImageUrl = null,
            platforms = listOf("GameCube", "Switch"),
            status = GameStatus.WANT_TO_PLAY,
            rating = null,
            completionDateEpochMs = null
        )

        repository.upsertGame(domainGame)

        repository.getGameById("g-new").test {
            val saved = awaitItem()
            assertEquals("Metroid Prime", saved?.title)
            assertEquals(GameStatus.WANT_TO_PLAY, saved?.status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteGame removes game by id`() = runTest {
        val domainGame = Game(
            id = "g-del",
            title = "To Delete",
            status = GameStatus.ABANDONED
        )
        repository.upsertGame(domainGame)
        repository.deleteGame("g-del")

        repository.getGameById("g-del").test {
            assertNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

class FakeGameDao : GameDao {
    private val gamesFlow = MutableStateFlow<Map<String, GameEntity>>(emptyMap())

    override suspend fun upsertGame(game: GameEntity) {
        gamesFlow.value = gamesFlow.value + (game.id to game)
    }

    override fun getGameById(id: String): Flow<GameEntity?> {
        return gamesFlow.map { it[id] }
    }

    override fun getAllGames(): Flow<List<GameEntity>> {
        return gamesFlow.map { it.values.toList() }
    }

    override fun getGamesByStatus(status: String): Flow<List<GameEntity>> {
        return gamesFlow.map { it.values.filter { g -> g.status == status } }
    }

    override suspend fun deleteGameById(id: String) {
        gamesFlow.value = gamesFlow.value - id
    }
}
