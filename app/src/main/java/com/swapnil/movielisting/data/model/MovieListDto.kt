package com.swapnil.movielisting.data.model

import com.swapnil.movielisting.domain.model.MovieList

data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)

fun MovieListDto.toMovieList(): MovieList {
    return MovieList(
        page = page,
        results = results.map { it.toMovie() },
        total_pages = total_pages,
        total_results = total_results
    )
}