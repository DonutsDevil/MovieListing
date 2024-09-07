package com.swapnil.movielisting.domain.usecase.listing

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