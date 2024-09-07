package com.swapnil.movielisting.data.remote

import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.util.Resource


interface MoviesRepository {
    suspend fun getMovies(): Resource<MovieList>
}