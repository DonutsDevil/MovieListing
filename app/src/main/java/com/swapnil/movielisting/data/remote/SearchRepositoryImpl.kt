package com.swapnil.movielisting.data.remote

import android.util.Log
import com.swapnil.movielisting.data.ApiClient
import com.swapnil.movielisting.data.model.listing.MovieListDto
import com.swapnil.movielisting.data.model.listing.toMovieList
import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.domain.model.search.Searchable
import com.swapnil.movielisting.domain.usecase.search.SearchRepository
import com.swapnil.movielisting.util.Resource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor (
    private val apiClient: ApiClient<MovieListDto>
): SearchRepository {
    private val TAG = "SearchRepositoryImpl"
    override suspend fun searchMovies(searchable: Searchable): Resource<MovieList> {
        return try {
            val result = apiClient.get("/3/search/movie", searchable.getQueryParameters())
            result.fold(
                onSuccess = { searchedMovies ->
                    Resource.Success(searchedMovies.toMovieList())
                },
                onFailure = {
                    Resource.Error("Something went wrong")
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "searchMovies: Exception occur when search: ${searchable.getQueryParameters()}", e)
            Resource.Error("Something went wrong")
        }
    }
}