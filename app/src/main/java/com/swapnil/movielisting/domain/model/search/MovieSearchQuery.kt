package com.swapnil.movielisting.domain.model.search

data class MovieSearchQuery(
    val movieName: String,
    val adultContent: Boolean = true
): Searchable {
    override fun getQueryParameters(): Map<String, String> {
        return mapOf(
            "query" to movieName,
            "include_adult" to adultContent.toString(),
            "language" to "en-US",
            "page" to 1.toString()
        )
    }
}