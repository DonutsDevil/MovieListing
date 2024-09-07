package com.swapnil.movielisting.data.model.preview

import com.swapnil.movielisting.domain.model.preview.MoviePreview
import kotlinx.serialization.SerialName

data class MoviePreviewDto(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdrop_path: String,
    @SerialName("id")
    val id: Int,
    @SerialName("imdb_id")
    val imdb_id: String,
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
    @SerialName("revenue")
    val revenue: Int,
    @SerialName("runtime")
    val runtime: Int,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val vote_average: Double,
    @SerialName("vote_count")
    val vote_count: Int
)

fun MoviePreviewDto.toMoviePreview() : MoviePreview {
    return MoviePreview(
        adult = adult,
        backdropPath = backdrop_path,
        id = id,
        imdbId = imdb_id,
        originalLanguage = original_language,
        originalTitle = original_title,
        overview = overview,
        popularity = popularity,
        posterPath = poster_path,
        releaseDate = release_date,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = vote_average,
        voteCount = vote_count
    )
}