package com.swapnil.movielisting.domain.usecase

import com.swapnil.movielisting.data.remote.MoviesRepository
import com.swapnil.movielisting.domain.model.GetMoviesUseCase
import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.util.Resource
import javax.inject.Inject

class GetMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MoviesRepository
) : GetMoviesUseCase {

    override suspend fun getMovies(): Resource<MovieList>{
        return movieRepository.getMovies()
    }
}