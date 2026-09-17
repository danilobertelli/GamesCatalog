package com.danilo.conductorexample.data.repository

import app.cash.turbine.test
import com.danilo.conductorexample.data.local.dao.PlatformDao
import com.danilo.conductorexample.data.local.entity.PlatformEntity
import com.danilo.conductorexample.data.local.preseed.PreseededPlatforms
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlatformRepositoryImplTest {

    private lateinit var fakeDao: FakePlatformDao
    private lateinit var repository: PlatformRepositoryImpl

    @Before
    fun setup() {
        fakeDao = FakePlatformDao()
        repository = PlatformRepositoryImpl(fakeDao)
    }

    @Test
    fun `getAllPlatforms maps entities to domain models`() = runTest {
        val entities = listOf(
            PlatformEntity("ps5", "PlayStation 5"),
            PlatformEntity("pc", "PC (Windows)")
        )
        fakeDao.insertPlatforms(entities)

        repository.getAllPlatforms().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("pc", list[0].id)
            assertEquals("PC (Windows)", list[0].name)
            assertEquals("ps5", list[1].id)
            assertEquals("PlayStation 5", list[1].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `seedPlatformsIfNeeded inserts preseeded platforms when database is empty`() = runTest {
        assertEquals(0, fakeDao.getPlatformCount())

        repository.seedPlatformsIfNeeded()

        assertEquals(PreseededPlatforms.list.size, fakeDao.getPlatformCount())
        assertTrue(fakeDao.insertedPlatforms.containsAll(PreseededPlatforms.list))
    }

    @Test
    fun `seedPlatformsIfNeeded does not insert when database already has platforms`() = runTest {
        val existing = listOf(PlatformEntity("switch", "Nintendo Switch"))
        fakeDao.insertPlatforms(existing)

        repository.seedPlatformsIfNeeded()

        assertEquals(1, fakeDao.getPlatformCount())
    }
}

class FakePlatformDao : PlatformDao {
    private val platformsFlow = MutableStateFlow<Map<String, PlatformEntity>>(emptyMap())
    val insertedPlatforms = mutableListOf<PlatformEntity>()

    override fun getAllPlatforms(): Flow<List<PlatformEntity>> {
        return platformsFlow.map { it.values.sortedBy { p -> p.name } }
    }

    override suspend fun insertPlatforms(platforms: List<PlatformEntity>) {
        insertedPlatforms.addAll(platforms)
        val current = platformsFlow.value.toMutableMap()
        platforms.forEach { current.putIfAbsent(it.id, it) }
        platformsFlow.value = current
    }

    override suspend fun getPlatformCount(): Int {
        return platformsFlow.value.size
    }
}
