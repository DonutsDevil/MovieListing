package com.swapnil.movielisting.data.model.listing

import com.swapnil.movielisting.domain.model.MovieListItem
import kotlinx.serialization.SerialName

data class MovieListItemDto(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdrop_path: String,
    @SerialName("id")
    val id: Int,
    @SerialName("original_language")
    val original_language: String,
    @SerialName("original_title")
    val original_title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val poster_path: String,
    @SerialName("release_date")
    val release_date: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val vote_average: Double,
    @SerialName("vote_count")
    val vote_count: Int
)

fun MovieListItemDto.toMovie() : MovieListItem {
    return MovieListItem(
        adult = adult,
        backdrop_path = backdrop_path,
        id = id,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count
    )
}