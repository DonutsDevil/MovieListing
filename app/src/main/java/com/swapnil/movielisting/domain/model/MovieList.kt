package com.swapnil.movielisting.domain.model

data class MovieList(
    val page: Int,
    val results: List<MovieListItem>,
    val total_pages: Int,
    val total_results: Int
)