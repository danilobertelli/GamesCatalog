package com.danilo.conductorexample.data.remote.datasource

import android.util.Log
import com.danilo.conductorexample.data.remote.api.IgdbApiService
import com.danilo.conductorexample.domain.model.GameSearchResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant
import java.time.ZoneId

/**
 * Remote data source abstraction for fetching game data from the IGDB API.
 */
interface IgdbRemoteDataSource {
    /**
     * Searches for video games matching the query string.
     *
     * @param query The search query string entered by the user.
     * @param limit Maximum number of results to fetch (defaults to 20).
     * @return [Result] encapsulating a list of [GameSearchResult] on success, or an exception on failure.
     */
    suspend fun searchGames(query: String, limit: Int = 20): Result<List<GameSearchResult>>
}

/**
 * Concrete implementation of [IgdbRemoteDataSource] using [IgdbApiService].
 *
 * Formats queries using Apicalypse syntax, executes the network call, and maps [com.danilo.conductorexample.data.remote.model.IgdbGameDto]
 * into domain [GameSearchResult] instances.
 *
 * @param apiService Retrofit API service for IGDB endpoints.
 */
class IgdbRemoteDataSourceImpl(
    private val apiService: IgdbApiService
) : IgdbRemoteDataSource {

    override suspend fun searchGames(query: String, limit: Int): Result<List<GameSearchResult>> {
        val sanitizedQuery = query.trim().replace("\"", "")
        if (sanitizedQuery.isBlank()) {
            return Result.success(emptyList())
        }

        Log.d(TAG, "Executing IGDB game search for query=\"$sanitizedQuery\" (limit=$limit)")

        val apicalypseQuery = buildString {
            append("fields name, summary, cover.image_id, cover.url, platforms.name, first_release_date, total_rating;\n")
            append("search \"$sanitizedQuery\";\n")
            append("limit $limit;\n")
        }

        val requestBody = apicalypseQuery.toRequestBody("text/plain".toMediaType())

        return runCatching {
            val dtoList = apiService.getGames(requestBody)
            dtoList.map { dto ->
                val coverUrl = dto.cover?.imageId?.let { imageId ->
                    buildCoverUrl(imageId)
                }

                val releaseYear = dto.firstReleaseDate?.let { epochSeconds ->
                    try {
                        Instant.ofEpochSecond(epochSeconds)
                            .atZone(ZoneId.systemDefault())
                            .year
                    } catch (_: Exception) {
                        null
                    }
                }

                GameSearchResult(
                    id = dto.id.toString(),
                    title = dto.name,
                    overview = dto.summary ?: "",
                    coverImageUrl = coverUrl,
                    platformNames = dto.platforms.map { it.name }.filter { it.isNotBlank() },
                    releaseYear = releaseYear
                )
            }
        }.onSuccess { results ->
            Log.d(TAG, "IGDB game search succeeded. Found ${results.size} matches for \"$sanitizedQuery\"")
        }.onFailure { error ->
            Log.e(TAG, "IGDB game search failed for query=\"$sanitizedQuery\"", error)
        }
    }

    companion object {
        private const val TAG = "IgdbRemoteDataSource"

        fun buildCoverUrl(imageId: String, size: String = "cover_big"): String {
            return "https://images.igdb.com/igdb/image/upload/t_$size/$imageId.jpg"
        }
    }
}
