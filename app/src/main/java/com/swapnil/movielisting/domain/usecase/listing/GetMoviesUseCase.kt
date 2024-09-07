package com.swapnil.movielisting.domain.usecase.listing

import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.util.Resource

interface GetMoviesUseCase {
    suspend fun getMovies(): Resource<MovieList>
}