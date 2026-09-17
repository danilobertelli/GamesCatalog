package com.danilo.conductorexample.data.remote.interceptor

import com.danilo.conductorexample.data.remote.auth.TwitchTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class IgdbAuthInterceptor(
    private val clientId: String,
    private val tokenManager: TwitchTokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking {
            tokenManager.getValidToken()
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Client-ID", clientId)
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
