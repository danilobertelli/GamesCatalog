package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network response model returned by Twitch OAuth2 `oauth2/token` endpoint.
 *
 * @property accessToken The bearer access token string.
 * @property expiresIn Token validity lifetime in seconds.
 * @property tokenType The authorization token type (e.g. "bearer").
 */
@Serializable
data class TwitchTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("token_type") val tokenType: String
)
