package com.danilo.conductorexample.data.repository

import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.entity.GameEntity
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GameRepositoryImpl(
    private val gameDao: GameDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {

    override fun getAllGames(): Flow<List<Game>> {
        return gameDao.getAllGames()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)
    }

    override fun getGamesByStatus(status: GameStatus): Flow<List<Game>> {
        return gameDao.getGamesByStatus(status.name)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)
    }

    override fun getGameById(id: String): Flow<Game?> {
        return gameDao.getGameById(id)
            .map { it?.toDomain() }
            .flowOn(ioDispatcher)
    }

    override suspend fun upsertGame(game: Game) = withContext(ioDispatcher) {
        gameDao.upsertGame(game.toEntity())
    }

    override suspend fun deleteGame(id: String) = withContext(ioDispatcher) {
        gameDao.deleteGameById(id)
    }

    private fun GameEntity.toDomain(): Game {
        val gameStatus = try {
            GameStatus.valueOf(status)
        } catch (_: IllegalArgumentException) {
            GameStatus.WANT_TO_PLAY
        }

        return Game(
            id = id,
            title = title,
            overview = overview,
            coverImageUrl = coverImageUrl,
            platforms = platforms,
            status = gameStatus,
            rating = rating,
            completionDateEpochMs = completionDateEpochMs
        )
    }

    private fun Game.toEntity(): GameEntity {
        return GameEntity(
            id = id,
            title = title,
            overview = overview,
            coverImageUrl = coverImageUrl,
            platforms = platforms,
            status = status.name,
            rating = rating,
            completionDateEpochMs = completionDateEpochMs
        )
    }
}
