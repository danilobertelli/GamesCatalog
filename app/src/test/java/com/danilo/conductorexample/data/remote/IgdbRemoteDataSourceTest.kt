package com.danilo.conductorexample.data.remote

import com.danilo.conductorexample.data.remote.api.IgdbApiService
import com.danilo.conductorexample.data.remote.datasource.IgdbRemoteDataSourceImpl
import com.danilo.conductorexample.data.remote.model.IgdbCoverDto
import com.danilo.conductorexample.data.remote.model.IgdbGameDto
import com.danilo.conductorexample.data.remote.model.IgdbPlatformDto
import kotlinx.coroutines.test.runTest
import okhttp3.RequestBody
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IgdbRemoteDataSourceTest {

    private class FakeIgdbApiService : IgdbApiService {
        var lastRequestBody: String? = null
        var shouldThrow = false
        var responseGames: List<IgdbGameDto> = emptyList()

        override suspend fun getGames(apicalypseQuery: RequestBody): List<IgdbGameDto> {
            val buffer = Buffer()
            apicalypseQuery.writeTo(buffer)
            lastRequestBody = buffer.readUtf8()

            if (shouldThrow) {
                throw RuntimeException("Network error")
            }
            return responseGames
        }
    }

    @Test
    fun searchGames_withBlankQuery_returnsEmptyWithoutNetworkCall() = runTest {
        val fakeApi = FakeIgdbApiService()
        val dataSource = IgdbRemoteDataSourceImpl(fakeApi)

        val result = dataSource.searchGames("   ")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
        assertEquals(null, fakeApi.lastRequestBody)
    }

    @Test
    fun searchGames_sendsApicalypseQueryAndMapsResultsCorrectly() = runTest {
        val fakeApi = FakeIgdbApiService().apply {
            responseGames = listOf(
                IgdbGameDto(
                    id = 1942L,
                    name = "The Witcher 3: Wild Hunt",
                    summary = "An open world RPG.",
                    cover = IgdbCoverDto(id = 10, imageId = "co1wyy"),
                    platforms = listOf(
                        IgdbPlatformDto(id = 6, name = "PC"),
                        IgdbPlatformDto(id = 48, name = "PlayStation 4")
                    ),
                    firstReleaseDate = 1431993600L, // 2015-05-19
                    totalRating = 92.5
                )
            )
        }
        val dataSource = IgdbRemoteDataSourceImpl(fakeApi)

        val result = dataSource.searchGames("Witcher", limit = 10)

        assertTrue(result.isSuccess)
        val games = result.getOrThrow()
        assertEquals(1, games.size)

        val game = games.first()
        assertEquals("1942", game.id)
        assertEquals("The Witcher 3: Wild Hunt", game.title)
        assertEquals("An open world RPG.", game.overview)
        assertEquals(
            "https://images.igdb.com/igdb/image/upload/t_cover_big/co1wyy.jpg",
            game.coverImageUrl
        )
        assertEquals(listOf("PC", "PlayStation 4"), game.platformNames)
        assertNotNull(game.releaseYear)

        // Verify sent Apicalypse body
        val body = fakeApi.lastRequestBody ?: ""
        assertTrue(body.contains("search \"Witcher\";"))
        assertTrue(body.contains("limit 10;"))
        assertTrue(body.contains("fields name, summary, cover.image_id"))
    }

    @Test
    fun searchGames_whenApiThrows_returnsFailure() = runTest {
        val fakeApi = FakeIgdbApiService().apply {
            shouldThrow = true
        }
        val dataSource = IgdbRemoteDataSourceImpl(fakeApi)

        val result = dataSource.searchGames("Zelda")

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
