package com.danilo.conductorexample.data.repository

import android.util.Log
import com.danilo.conductorexample.data.local.dao.PlatformDao
import com.danilo.conductorexample.data.local.preseed.PreseededPlatforms
import com.danilo.conductorexample.domain.model.Platform
import com.danilo.conductorexample.domain.repository.PlatformRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of [PlatformRepository] using Room's [PlatformDao].
 *
 * Handles platform querying and database seeding on first launch from [PreseededPlatforms].
 *
 * @param platformDao Room DAO for platform table operations.
 * @param ioDispatcher Coroutine dispatcher for background persistence tasks.
 */
class PlatformRepositoryImpl(
    private val platformDao: PlatformDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlatformRepository {

    override fun getAllPlatforms(): Flow<List<Platform>> {
        return platformDao.getAllPlatforms()
            .map { list ->
                list.map { entity ->
                    Platform(
                        id = entity.id,
                        name = entity.name
                    )
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun seedPlatformsIfNeeded() {
        withContext(ioDispatcher) {
            val count = platformDao.getPlatformCount()
            if (count == 0) {
                Log.d(TAG, "No platforms found in database. Seeding ${PreseededPlatforms.list.size} default platforms...")
                try {
                    platformDao.insertPlatforms(PreseededPlatforms.list)
                    Log.d(TAG, "Default platforms seeded successfully.")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to seed default platforms", e)
                    throw e
                }
            } else {
                Log.d(TAG, "Platform database already initialized ($count platforms found).")
            }
        }
    }

    companion object {
        private const val TAG = "PlatformRepository"
    }
}
