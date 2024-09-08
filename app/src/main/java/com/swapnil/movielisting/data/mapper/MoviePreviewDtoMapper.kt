package com.swapnil.movielisting.data.mapper

import com.google.gson.Gson
import com.swapnil.movielisting.data.model.preview.MoviePreviewDto

class MoviePreviewDtoMapper: ResponseMapper<MoviePreviewDto>() {
    override fun map(value: String): MoviePreviewDto {
        return Gson().fromJson(value, MoviePreviewDto::class.java)
    }
}