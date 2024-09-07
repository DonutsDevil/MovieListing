package com.swapnil.movielisting.domain.model

import com.swapnil.movielisting.util.Resource

interface GetMoviesUseCase {
    suspend fun getMovies(): Resource<MovieList>
}