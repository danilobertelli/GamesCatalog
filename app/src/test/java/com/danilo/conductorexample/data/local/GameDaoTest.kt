package com.danilo.conductorexample.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.database.GamesCatalogDatabase
import com.danilo.conductorexample.data.local.entity.GameEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GameDaoTest {

    private lateinit var database: GamesCatalogDatabase
    private lateinit var gameDao: GameDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GamesCatalogDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        gameDao = database.gameDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetGameById() = runTest {
        val entity = GameEntity(
            id = "g-1",
            title = "Chrono Trigger",
            overview = "Classic JRPG",
            coverImageUrl = "https://example.com/chrono.jpg",
            platforms = listOf("SNES", "PC"),
            status = "COMPLETED",
            rating = 5,
            completionDateEpochMs = 1700000000000L
        )

        gameDao.upsertGame(entity)

        gameDao.getGameById("g-1").test {
            val item = awaitItem()
            assertEquals("Chrono Trigger", item?.title)
            assertEquals("COMPLETED", item?.status)
            assertEquals(5, item?.rating)
            assertEquals(listOf("SNES", "PC"), item?.platforms)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun filterGamesByStatus() = runTest {
        val g1 = GameEntity(
            id = "g-1",
            title = "Elden Ring",
            overview = "Action RPG",
            coverImageUrl = null,
            platforms = listOf("PC", "PS5"),
            status = "PLAYING",
            rating = 5,
            completionDateEpochMs = null
        )
        val g2 = GameEntity(
            id = "g-2",
            title = "Hollow Knight",
            overview = "Metroidvania",
            coverImageUrl = null,
            platforms = listOf("PC", "Switch"),
            status = "COMPLETED",
            rating = 5,
            completionDateEpochMs = 1690000000000L
        )

        gameDao.upsertGame(g1)
        gameDao.upsertGame(g2)

        gameDao.getGamesByStatus("PLAYING").test {
            val list = awaitItem()
            assertEquals(1, list.size)
            assertEquals("Elden Ring", list[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteGameById() = runTest {
        val entity = GameEntity(
            id = "g-delete",
            title = "Test Game",
            overview = "",
            coverImageUrl = null,
            platforms = emptyList(),
            status = "WANT_TO_PLAY",
            rating = null,
            completionDateEpochMs = null
        )

        gameDao.upsertGame(entity)
        gameDao.deleteGameById("g-delete")

        gameDao.getGameById("g-delete").test {
            val item = awaitItem()
            assertNull(item)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
