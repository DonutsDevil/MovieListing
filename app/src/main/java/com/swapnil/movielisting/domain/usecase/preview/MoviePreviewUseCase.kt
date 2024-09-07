package com.swapnil.movielisting.domain.usecase.preview

import com.swapnil.movielisting.domain.model.preview.MoviePreview
import com.swapnil.movielisting.util.Resource

interface MoviePreviewUseCase {
    suspend fun getPreview(selectedMovieId: Int): Resource<MoviePreview>
}