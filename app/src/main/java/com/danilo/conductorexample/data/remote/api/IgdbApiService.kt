package com.danilo.conductorexample.data.remote.api

import com.danilo.conductorexample.data.remote.model.IgdbGameDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service definition for querying the IGDB API v4 `/games` endpoint.
 *
 * Requests use POST with an Apicalypse query string transmitted as `text/plain` in the request body.
 */
interface IgdbApiService {
    /**
     * Executes an Apicalypse query against the IGDB games catalog.
     *
     * @param apicalypseQuery The Apicalypse query DSL encapsulated in an OkHttp [RequestBody].
     * @return List of [IgdbGameDto] matching the query parameters.
     */
    @POST("games")
    suspend fun getGames(
        @Body apicalypseQuery: RequestBody
    ): List<IgdbGameDto>
}
