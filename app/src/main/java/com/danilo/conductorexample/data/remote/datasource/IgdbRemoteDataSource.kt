package com.danilo.conductorexample.data.remote.datasource

import com.danilo.conductorexample.data.remote.api.IgdbApiService
import com.danilo.conductorexample.domain.model.GameSearchResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant
import java.time.ZoneId

interface IgdbRemoteDataSource {
    suspend fun searchGames(query: String, limit: Int = 20): Result<List<GameSearchResult>>
}

class IgdbRemoteDataSourceImpl(
    private val apiService: IgdbApiService
) : IgdbRemoteDataSource {

    override suspend fun searchGames(query: String, limit: Int): Result<List<GameSearchResult>> {
        val sanitizedQuery = query.trim().replace("\"", "")
        if (sanitizedQuery.isBlank()) {
            return Result.success(emptyList())
        }

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
        }
    }

    companion object {
        fun buildCoverUrl(imageId: String, size: String = "cover_big"): String {
            return "https://images.igdb.com/igdb/image/upload/t_$size/$imageId.jpg"
        }
    }
}
