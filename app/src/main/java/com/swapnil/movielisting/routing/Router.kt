package com.swapnil.movielisting.routing

sealed class Router(val key: String) {

    data object MovieFeeds : Router("movie_feeds")

    data class MovieDetails(val id: Int) : Router("movie_details/$id") {
        companion object {
            const val ID = "id"
            const val key = "movie_details/{$ID}"
        }
    }
}