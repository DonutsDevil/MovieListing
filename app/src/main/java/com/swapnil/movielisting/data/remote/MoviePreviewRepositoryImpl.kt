package com.swapnil.movielisting.data.remote

import android.util.Log
import com.swapnil.movielisting.data.ApiClient
import com.swapnil.movielisting.data.model.preview.MoviePreviewDto
import com.swapnil.movielisting.data.model.preview.toMoviePreview
import com.swapnil.movielisting.domain.model.preview.MoviePreview
import com.swapnil.movielisting.domain.usecase.preview.MoviePreviewRepository
import com.swapnil.movielisting.util.Resource
import javax.inject.Inject

class MoviePreviewRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient<MoviePreviewDto>
) : MoviePreviewRepository {
    private val TAG = "MoviePreviewRepositoryI"

    override suspend fun getPreview(selectedMovieId: Int): Resource<MoviePreview> {
        return try {
            val result = apiClient.get("/3/movie/$selectedMovieId?language=en-US")

            result.fold(
                onSuccess = {
                    Resource.Success(it.toMoviePreview())
                }, onFailure = {
                    Resource.Error("Something went wrong")
                }
            )

        }  catch (e: Exception) {
            Log.e(TAG, "getPreview: error fetching preview", e)
            Resource.Error("Something went wrong")
        }
    }
}