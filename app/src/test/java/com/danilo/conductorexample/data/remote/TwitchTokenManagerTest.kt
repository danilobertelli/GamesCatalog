package com.danilo.conductorexample.data.remote

import com.danilo.conductorexample.data.remote.api.TwitchAuthService
import com.danilo.conductorexample.data.remote.auth.TwitchTokenManagerImpl
import com.danilo.conductorexample.data.remote.model.TwitchTokenResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TwitchTokenManagerTest {

    private class FakeTwitchAuthService : TwitchAuthService {
        var callCount = 0
        var returnToken = "token_1"
        var expiresInSeconds = 3600L

        override suspend fun getAccessToken(
            clientId: String,
            clientSecret: String,
            grantType: String
        ): TwitchTokenResponse {
            callCount++
            return TwitchTokenResponse(
                accessToken = returnToken,
                expiresIn = expiresInSeconds,
                tokenType = "bearer"
            )
        }
    }

    @Test
    fun getValidToken_fetchesFromServiceOnFirstCall() = runTest {
        val fakeService = FakeTwitchAuthService()
        val tokenManager = TwitchTokenManagerImpl(
            twitchAuthService = fakeService,
            clientId = "test_client_id",
            clientSecret = "test_secret",
            timeProvider = { 1000L }
        )

        val token = tokenManager.getValidToken()

        assertEquals("token_1", token)
        assertEquals(1, fakeService.callCount)
    }

    @Test
    fun getValidToken_reusesCachedTokenWithinExpirationWindow() = runTest {
        val fakeService = FakeTwitchAuthService()
        var simulatedTime = 1000L
        val tokenManager = TwitchTokenManagerImpl(
            twitchAuthService = fakeService,
            clientId = "test_client_id",
            clientSecret = "test_secret",
            timeProvider = { simulatedTime }
        )

        val tokenFirst = tokenManager.getValidToken()
        simulatedTime += 100_000L // Advanced 100s, well within 3600s
        val tokenSecond = tokenManager.getValidToken()

        assertEquals("token_1", tokenFirst)
        assertEquals("token_1", tokenSecond)
        assertEquals(1, fakeService.callCount)
    }

    @Test
    fun getValidToken_refreshesWhenExpired() = runTest {
        val fakeService = FakeTwitchAuthService()
        var simulatedTime = 1000L
        val tokenManager = TwitchTokenManagerImpl(
            twitchAuthService = fakeService,
            clientId = "test_client_id",
            clientSecret = "test_secret",
            timeProvider = { simulatedTime }
        )

        tokenManager.getValidToken()
        assertEquals(1, fakeService.callCount)

        // Advance time past expiry (3600s = 3_600_000ms)
        fakeService.returnToken = "token_2"
        simulatedTime += 3_600_000L

        val newToken = tokenManager.getValidToken()
        assertEquals("token_2", newToken)
        assertEquals(2, fakeService.callCount)
    }
}
