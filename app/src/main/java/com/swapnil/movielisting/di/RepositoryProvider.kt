package com.swapnil.movielisting.di

import android.util.Log
import com.swapnil.movielisting.BuildConfig
import com.swapnil.movielisting.data.KtorApiClient
import com.swapnil.movielisting.data.RemoteRoutes
import com.swapnil.movielisting.data.mapper.MovieListDtoMapper
import com.swapnil.movielisting.data.mapper.MoviePreviewDtoMapper
import com.swapnil.movielisting.data.remote.MoviePreviewRepositoryImpl
import com.swapnil.movielisting.domain.usecase.listing.MoviesRepository
import com.swapnil.movielisting.data.remote.MoviesRepositoryImpl
import com.swapnil.movielisting.data.remote.SearchRepositoryImpl
import com.swapnil.movielisting.domain.usecase.preview.MoviePreviewRepository
import com.swapnil.movielisting.domain.usecase.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvider {


    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO



    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {

            expectSuccess = false

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    encodeDefaults = true
                    explicitNulls = false
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                this.level = LogLevel.ALL
                this.logger = Logger.Companion.SIMPLE
            }

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { cause, request ->
                    Log.e("HttpCallValidator", "handleResponseExceptionWithRequest: $cause - $request")
                }
            }

            install(HttpRequestRetry) {
                maxRetries = 3
                delayMillis { retry ->
                    20L
                }
                retryIf { request, response ->
                    response.status.value >= 500
                }
                retryOnExceptionIf { request, cause ->
                    true
                }
            }

            defaultRequest {
                accept(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_AUTH_TOKEN}")
            }
        }
    }


    @Provides
    @Singleton
    fun provideMovieListRepository(httpClient: HttpClient): MoviesRepository {
        return MoviesRepositoryImpl(
            httpClient = KtorApiClient(
                baseUrl = RemoteRoutes.TMDB_BASE_URL,
                responseMapper = MovieListDtoMapper(),
                httpClient = httpClient
            )
        )
    }

    @Provides
    @Singleton
    fun provideMoviePreviewRepository(httpClient: HttpClient): MoviePreviewRepository {
        return MoviePreviewRepositoryImpl(
            apiClient = KtorApiClient(
                baseUrl = RemoteRoutes.TMDB_BASE_URL,
                responseMapper = MoviePreviewDtoMapper(),
                httpClient = httpClient
            )
        )
    }

    @Provides
    @Singleton
    fun provideSearchRepository(httpClient: HttpClient): SearchRepository {
        return SearchRepositoryImpl(
            apiClient = KtorApiClient(
                baseUrl = RemoteRoutes.TMDB_BASE_URL,
                responseMapper = MovieListDtoMapper(),
                httpClient = httpClient
            )
        )
    }
}