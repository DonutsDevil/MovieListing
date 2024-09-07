package com.swapnil.movielisting.di

import com.swapnil.movielisting.domain.usecase.listing.MoviesRepository
import com.swapnil.movielisting.domain.usecase.listing.GetMoviesUseCase
import com.swapnil.movielisting.domain.usecase.listing.GetMoviesUseCaseImpl
import com.swapnil.movielisting.domain.usecase.preview.MoviePreviewRepository
import com.swapnil.movielisting.domain.usecase.preview.MoviePreviewUseCase
import com.swapnil.movielisting.domain.usecase.preview.MoviePreviewUseCaseImpl
import com.swapnil.movielisting.domain.usecase.search.SearchRepository
import com.swapnil.movielisting.domain.usecase.search.SearchUseCase
import com.swapnil.movielisting.domain.usecase.search.SearchUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseProvider {

    @Provides
    fun provideMoviesUseCase(moviesRepository: MoviesRepository): GetMoviesUseCase {
        return GetMoviesUseCaseImpl(moviesRepository)
    }

    @Provides
    fun provideMoviePreviewUseCase(moviePreviewRepository: MoviePreviewRepository): MoviePreviewUseCase {
        return MoviePreviewUseCaseImpl(moviePreviewRepository)
    }

    @Provides
    fun provideSearchUseCase(searchRepository: SearchRepository): SearchUseCase {
        return SearchUseCaseImpl(searchRepository)
    }
}