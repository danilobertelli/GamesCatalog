package com.danilo.conductorexample.data.remote.auth

import android.util.Log
import com.danilo.conductorexample.data.remote.api.TwitchAuthService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manager contract responsible for supplying a valid Twitch OAuth2 access token
 * for authenticating against IGDB API endpoints.
 */
interface TwitchTokenManager {
    /**
     * Retrieves an active OAuth token, fetching a fresh token or returning a cached one
     * if it has not yet expired.
     */
    suspend fun getValidToken(): String
}

/**
 * Thread-safe implementation of [TwitchTokenManager] using a coroutine [Mutex].
 *
 * Caches tokens in-memory and refreshes them automatically when within a 60-second
 * safety buffer of their expiration time, preventing concurrent duplicate token requests.
 *
 * @param twitchAuthService Retrofit service to request new tokens.
 * @param clientId Twitch Developer Client ID.
 * @param clientSecret Twitch Developer Client Secret.
 * @param timeProvider Function returning the current system time in milliseconds (injectable for unit tests).
 */
class TwitchTokenManagerImpl(
    private val twitchAuthService: TwitchAuthService,
    private val clientId: String,
    private val clientSecret: String,
    private val timeProvider: () -> Long = { System.currentTimeMillis() }
) : TwitchTokenManager {

    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var tokenExpiresAtEpochMs: Long = 0L

    override suspend fun getValidToken(): String = mutex.withLock {
        val currentTime = timeProvider()
        val currentToken = cachedToken

        // Reuse cached token if valid with 60 second safety buffer
        if (currentToken != null && currentTime < (tokenExpiresAtEpochMs - 60_000L)) {
            val remainingSec = (tokenExpiresAtEpochMs - currentTime) / 1000
            Log.d(TAG, "Reusing cached Twitch OAuth token. Expires in: ${remainingSec}s")
            return@withLock currentToken
        }

        Log.d(TAG, "Fetching new Twitch OAuth token via client_credentials flow...")
        val response = try {
            twitchAuthService.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch Twitch OAuth access token", e)
            throw e
        }

        cachedToken = response.accessToken
        tokenExpiresAtEpochMs = currentTime + (response.expiresIn * 1000L)
        Log.d(TAG, "Successfully acquired Twitch OAuth token. Valid for: ${response.expiresIn}s")

        response.accessToken
    }

    companion object {
        private const val TAG = "TwitchTokenManager"
    }
}
