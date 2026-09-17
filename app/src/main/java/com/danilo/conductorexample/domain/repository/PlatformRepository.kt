package com.danilo.conductorexample.domain.repository

import com.danilo.conductorexample.domain.model.Platform
import kotlinx.coroutines.flow.Flow

/**
 * Contract defining data access operations for available gaming platforms.
 */
interface PlatformRepository {
    /**
     * Observes the complete list of available gaming platforms in the database.
     *
     * @return A cold [Flow] emitting the updated list of [Platform]s.
     */
    fun getAllPlatforms(): Flow<List<Platform>>

    /**
     * Seeds initial default gaming platforms (e.g. PC, PS5, Xbox Series X, Switch)
     * if the underlying platform table is currently empty.
     */
    suspend fun seedPlatformsIfNeeded()
}
