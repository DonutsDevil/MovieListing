package com.swapnil.movielisting.domain.usecase.search

import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.domain.model.search.Searchable
import com.swapnil.movielisting.util.Resource
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository
): SearchUseCase {

    override suspend fun searchMovies(searchable: Searchable): Resource<MovieList> {
        return searchRepository.searchMovies(searchable)
    }

}