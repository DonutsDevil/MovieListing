package com.swapnil.movielisting.data.mapper

import com.google.gson.Gson
import com.swapnil.movielisting.data.model.listing.MovieListDto

class MovieListDtoMapper : ResponseMapper<MovieListDto>() {
    override fun map(value: String): MovieListDto {
        return Gson().fromJson(value, MovieListDto::class.java)
    }
}