package com.danilo.conductorexample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danilo.conductorexample.data.local.entity.PlatformEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for managing [PlatformEntity] records.
 */
@Dao
interface PlatformDao {

    /**
     * Queries all platforms stored in the database, ordered alphabetically by name.
     *
     * @return A cold [Flow] emitting the list of [PlatformEntity] instances.
     */
    @Query("SELECT * FROM platforms ORDER BY name ASC")
    fun getAllPlatforms(): Flow<List<PlatformEntity>>

    /**
     * Inserts a list of platform records, ignoring conflicting rows with the same primary key.
     *
     * @param platforms The list of [PlatformEntity] records to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlatforms(platforms: List<PlatformEntity>)

    /**
     * Counts the total number of platforms currently saved in the database.
     *
     * @return The platform row count.
     */
    @Query("SELECT COUNT(*) FROM platforms")
    suspend fun getPlatformCount(): Int
}
