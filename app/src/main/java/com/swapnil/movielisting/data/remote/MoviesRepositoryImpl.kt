package com.swapnil.movielisting.data.remote

import android.util.Log
import com.google.gson.Gson
import com.swapnil.movielisting.data.ApiClient
import com.swapnil.movielisting.data.mapper.ResponseMapper
import com.swapnil.movielisting.data.model.MovieListDto
import com.swapnil.movielisting.data.model.toMovieList
import com.swapnil.movielisting.domain.model.MovieList
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

class MovieListDtoMapper : ResponseMapper<MovieListDto>() {
    override fun map(value: String): MovieListDto {
        return Gson().fromJson(value, MovieListDto::class.java)
    }
}