package com.danilo.conductorexample.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.danilo.conductorexample.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Upsert
    suspend fun upsertGame(game: GameEntity)

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: String): Flow<GameEntity?>

    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE status = :status ORDER BY title ASC")
    fun getGamesByStatus(status: String): Flow<List<GameEntity>>

    @Query("DELETE FROM games WHERE id = :id")
    suspend fun deleteGameById(id: String)
}
