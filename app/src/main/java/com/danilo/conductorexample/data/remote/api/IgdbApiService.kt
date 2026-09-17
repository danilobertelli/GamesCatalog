package com.danilo.conductorexample.data.remote.api

import com.danilo.conductorexample.data.remote.model.IgdbGameDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface IgdbApiService {
    @POST("games")
    suspend fun getGames(
        @Body apicalypseQuery: RequestBody
    ): List<IgdbGameDto>
}
