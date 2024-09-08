package com.swapnil.movielisting.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.swapnil.movielisting.di.IoDispatcher
import com.swapnil.movielisting.domain.usecase.listing.GetMoviesUseCase
import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.util.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListUseCase: GetMoviesUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): BaseViewModel<MovieListState, MovieListAction, Unit>() {

    override val _state: MutableStateFlow<MovieListState> = MutableStateFlow(getInitialValue())
    override val state: StateFlow<MovieListState> = _state.asStateFlow()

    override val _effect: MutableSharedFlow<Unit> = MutableSharedFlow()
    override val effect: SharedFlow<Unit> = _effect.asSharedFlow()

    override fun processAction(action: MovieListAction) {
        when(action) {
            is MovieListAction.OnLaunch -> {
                getMovies()
            }
        }
    }

    private fun getInitialValue(): MovieListState {
        return MovieListState.getInitialState()
    }

    private fun getMovies() {
        setState(getValue().copy(isLoading = true))
        viewModelScope.launch(dispatcher) {
            val resource = movieListUseCase.getMovies()

            resource.fold(
                onSuccess = { movies ->
                    setState(getValue().copy(isLoading = false, movies = movies, error = null))
                },
                onError = { error ->
                    setState(getValue().copy(isLoading = false, error = error))
                }
            )
        }
    }

}

sealed interface MovieListAction {
    data object OnLaunch: MovieListAction
}

data class MovieListState(
    val isLoading: Boolean,
    val error: String?,
    val movies: MovieList?
) {
    companion object {
        fun getInitialState(): MovieListState {
            return MovieListState(
                isLoading = false,
                error = null,
                movies = null
            )
        }
    }

    fun isMoviesAvailable(): Boolean {
        return movies != null
    }
}