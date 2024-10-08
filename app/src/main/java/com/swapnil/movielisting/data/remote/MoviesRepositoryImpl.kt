package com.swapnil.movielisting.data.remote

import android.util.Log
import com.swapnil.movielisting.data.ApiClient
import com.swapnil.movielisting.data.model.listing.MovieListDto
import com.swapnil.movielisting.data.model.listing.toMovieList
import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.domain.usecase.listing.MoviesRepository
import com.swapnil.movielisting.util.Resource

class MoviesRepositoryImpl(
    private val httpClient: ApiClient<MovieListDto>
) : MoviesRepository {
    private val TAG = "MoviesRepositoryImpl"
    override suspend fun getMovies(): Resource<MovieList> {
        return try {
            val result = httpClient.get("/3/trending/movie/day?language=en-US")

            result.fold(
                onSuccess = {
                    Resource.Success(it.toMovieList())
                },
                onFailure = {
                    Resource.Error("Something went wrong")
                }
            )

        } catch (e: Exception) {
            Log.e(TAG, "getMovies: Exception Occurred", e)
            Resource.Error("Something went wrong")
        }
    }
}