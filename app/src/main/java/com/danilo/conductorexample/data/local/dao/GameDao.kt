package com.danilo.conductorexample.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.danilo.conductorexample.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for managing [GameEntity] records in the database.
 */
@Dao
interface GameDao {

    /**
     * Inserts or updates a game record in the database.
     *
     * @param game The [GameEntity] to persist.
     */
    @Upsert
    suspend fun upsertGame(game: GameEntity)

    /**
     * Queries a game record by its unique primary key.
     *
     * @param id The game ID.
     * @return A cold [Flow] emitting the matching [GameEntity], or null if none exists.
     */
    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: String): Flow<GameEntity?>

    /**
     * Queries all stored game records sorted alphabetically by title.
     *
     * @return A cold [Flow] emitting the list of all [GameEntity] records.
     */
    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<GameEntity>>

    /**
     * Queries game records matching a given status string, sorted by title.
     *
     * @param status The string name of the status (e.g. "PLAYING", "COMPLETED").
     * @return A cold [Flow] emitting matching [GameEntity] records.
     */
    @Query("SELECT * FROM games WHERE status = :status ORDER BY title ASC")
    fun getGamesByStatus(status: String): Flow<List<GameEntity>>

    /**
     * Deletes a game record matching the provided identifier.
     *
     * @param id The game ID to delete.
     */
    @Query("DELETE FROM games WHERE id = :id")
    suspend fun deleteGameById(id: String)
}
