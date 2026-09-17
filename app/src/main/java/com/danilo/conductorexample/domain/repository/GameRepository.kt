package com.danilo.conductorexample.domain.repository

import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllGames(): Flow<List<Game>>
    fun getGamesByStatus(status: GameStatus): Flow<List<Game>>
    fun getGameById(id: String): Flow<Game?>
    suspend fun upsertGame(game: Game)
    suspend fun deleteGame(id: String)
}
