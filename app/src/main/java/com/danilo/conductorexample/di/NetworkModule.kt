package com.danilo.conductorexample.di

import com.danilo.conductorexample.BuildConfig
import com.danilo.conductorexample.data.remote.api.IgdbApiService
import com.danilo.conductorexample.data.remote.api.TwitchAuthService
import com.danilo.conductorexample.data.remote.auth.TwitchTokenManager
import com.danilo.conductorexample.data.remote.auth.TwitchTokenManagerImpl
import com.danilo.conductorexample.data.remote.datasource.IgdbRemoteDataSource
import com.danilo.conductorexample.data.remote.datasource.IgdbRemoteDataSourceImpl
import com.danilo.conductorexample.data.remote.interceptor.IgdbAuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Twitch OAuth Client
    single(named("twitchOkHttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named("twitchRetrofit")) {
        val json = get<Json>()
        Retrofit.Builder()
            .baseUrl("https://id.twitch.tv/")
            .client(get(named("twitchOkHttp")))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<TwitchAuthService> {
        get<Retrofit>(named("twitchRetrofit")).create(TwitchAuthService::class.java)
    }

    single<TwitchTokenManager> {
        TwitchTokenManagerImpl(
            twitchAuthService = get(),
            clientId = BuildConfig.IGDB_CLIENT_ID,
            clientSecret = BuildConfig.IGDB_CLIENT_SECRET
        )
    }

    // IGDB API Client
    single {
        IgdbAuthInterceptor(
            clientId = BuildConfig.IGDB_CLIENT_ID,
            tokenManager = get()
        )
    }

    single(named("igdbOkHttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get<IgdbAuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named("igdbRetrofit")) {
        val json = get<Json>()
        Retrofit.Builder()
            .baseUrl("https://api.igdb.com/v4/")
            .client(get(named("igdbOkHttp")))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<IgdbApiService> {
        get<Retrofit>(named("igdbRetrofit")).create(IgdbApiService::class.java)
    }

    single<IgdbRemoteDataSource> {
        IgdbRemoteDataSourceImpl(apiService = get())
    }
}
