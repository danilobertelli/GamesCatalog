package com.danilo.conductorexample.data.repository

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
            if (platformDao.getPlatformCount() == 0) {
                platformDao.insertPlatforms(PreseededPlatforms.list)
            }
        }
    }
}
