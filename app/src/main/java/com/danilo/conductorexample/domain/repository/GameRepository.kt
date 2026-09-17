package com.danilo.conductorexample.domain.repository

import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow

/**
 * Contract defining persistent data access operations for user games.
 *
 * Provides reactive read streams using Kotlin [Flow] and asynchronous write operations
 * via suspending functions.
 */
interface GameRepository {
    /**
     * Observes the complete list of games stored in the catalog.
     *
     * @return A cold [Flow] emitting the updated list of [Game]s whenever changes occur.
     */
    fun getAllGames(): Flow<List<Game>>

    /**
     * Observes games filtered by their play status.
     *
     * @param status The [GameStatus] to filter games by.
     * @return A cold [Flow] emitting games matching the specified status.
     */
    fun getGamesByStatus(status: GameStatus): Flow<List<Game>>

    /**
     * Observes a single game by its unique identifier.
     *
     * @param id The unique identifier of the game.
     * @return A cold [Flow] emitting the [Game] if found, or null if deleted/not present.
     */
    fun getGameById(id: String): Flow<Game?>

    /**
     * Inserts or updates a game record in the persistent storage.
     *
     * @param game The [Game] domain model to save.
     */
    suspend fun upsertGame(game: Game)

    /**
     * Deletes a game record from persistent storage by its identifier.
     *
     * @param id The unique identifier of the game to delete.
     */
    suspend fun deleteGame(id: String)
}
