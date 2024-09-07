package com.swapnil.movielisting.domain.usecase.preview

import com.swapnil.movielisting.domain.model.preview.MoviePreview
import com.swapnil.movielisting.util.Resource
import javax.inject.Inject

class MoviePreviewUseCaseImpl @Inject constructor(
    private val moviePreviewRepository: MoviePreviewRepository
) : MoviePreviewUseCase {
    override suspend fun getPreview(selectedMovieId: Int): Resource<MoviePreview> {
        return moviePreviewRepository.getPreview(selectedMovieId)
    }
}