package com.danilo.conductorexample.data.remote.api

import com.danilo.conductorexample.data.remote.model.TwitchTokenResponse
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit service definition for Twitch OAuth2 authentication endpoints.
 *
 * Used to exchange Twitch Client ID and Client Secret for short-lived App Access Tokens
 * required by the IGDB API.
 */
interface TwitchAuthService {
    /**
     * Obtains an App Access Token using the OAuth2 `client_credentials` grant flow.
     *
     * @param clientId The Twitch Developer application client identifier.
     * @param clientSecret The Twitch Developer application client secret.
     * @param grantType The OAuth grant type, defaulting to "client_credentials".
     * @return [TwitchTokenResponse] containing the access token and expiration time.
     */
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("grant_type") grantType: String = "client_credentials"
    ): TwitchTokenResponse
}
