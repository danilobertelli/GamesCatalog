package com.danilo.conductorexample.data.remote.api

import com.danilo.conductorexample.data.remote.model.TwitchTokenResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitchAuthService {
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("grant_type") grantType: String = "client_credentials"
    ): TwitchTokenResponse
}
