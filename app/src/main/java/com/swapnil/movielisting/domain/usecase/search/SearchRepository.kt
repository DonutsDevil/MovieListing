package com.swapnil.movielisting.domain.usecase.search

import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.domain.model.search.Searchable
import com.swapnil.movielisting.util.Resource

interface SearchRepository {
    suspend fun searchMovies(searchable: Searchable): Resource<MovieList>
}