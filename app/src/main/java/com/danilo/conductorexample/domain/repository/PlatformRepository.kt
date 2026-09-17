package com.danilo.conductorexample.domain.repository

import com.danilo.conductorexample.domain.model.Platform
import kotlinx.coroutines.flow.Flow

interface PlatformRepository {
    fun getAllPlatforms(): Flow<List<Platform>>
    suspend fun seedPlatformsIfNeeded()
}
