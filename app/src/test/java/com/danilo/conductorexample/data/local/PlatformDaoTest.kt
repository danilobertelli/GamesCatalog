package com.danilo.conductorexample.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.danilo.conductorexample.data.local.dao.PlatformDao
import com.danilo.conductorexample.data.local.database.GamesCatalogDatabase
import com.danilo.conductorexample.data.local.entity.PlatformEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlatformDaoTest {

    private lateinit var database: GamesCatalogDatabase
    private lateinit var platformDao: PlatformDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GamesCatalogDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        platformDao = database.platformDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertPlatformsAndGetAllOrderedByName() = runTest {
        val p1 = PlatformEntity(id = "ps5", name = "PlayStation 5")
        val p2 = PlatformEntity(id = "switch", name = "Nintendo Switch")
        val p3 = PlatformEntity(id = "xbox", name = "Xbox Series X/S")

        platformDao.insertPlatforms(listOf(p1, p2, p3))

        platformDao.getAllPlatforms().test {
            val list = awaitItem()
            assertEquals(3, list.size)
            // Alphabetical check: Nintendo Switch, PlayStation 5, Xbox Series X/S
            assertEquals("Nintendo Switch", list[0].name)
            assertEquals("PlayStation 5", list[1].name)
            assertEquals("Xbox Series X/S", list[2].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPlatformCountReturnsAccurateNumber() = runTest {
        assertEquals(0, platformDao.getPlatformCount())

        platformDao.insertPlatforms(
            listOf(
                PlatformEntity(id = "pc", name = "PC (Windows)"),
                PlatformEntity(id = "mac", name = "Mac")
            )
        )

        assertEquals(2, platformDao.getPlatformCount())
    }
}
