package com.swapnil.movielisting.di

import com.swapnil.movielisting.domain.usecase.listing.MoviesRepository
import com.swapnil.movielisting.domain.usecase.listing.GetMoviesUseCase
import com.swapnil.movielisting.domain.usecase.listing.GetMoviesUseCaseImpl
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
}