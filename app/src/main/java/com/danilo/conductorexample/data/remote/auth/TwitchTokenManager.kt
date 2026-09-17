package com.danilo.conductorexample.data.remote.auth

import com.danilo.conductorexample.data.remote.api.TwitchAuthService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface TwitchTokenManager {
    suspend fun getValidToken(): String
}

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
            return@withLock currentToken
        }

        val response = twitchAuthService.getAccessToken(
            clientId = clientId,
            clientSecret = clientSecret
        )

        cachedToken = response.accessToken
        tokenExpiresAtEpochMs = currentTime + (response.expiresIn * 1000L)

        response.accessToken
    }
}
