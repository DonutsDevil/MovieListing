package com.swapnil.movielisting.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.swapnil.movielisting.di.IoDispatcher
import com.swapnil.movielisting.domain.model.MovieList
import com.swapnil.movielisting.domain.model.search.MovieSearchQuery
import com.swapnil.movielisting.domain.usecase.search.SearchUseCase
import com.swapnil.movielisting.util.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): BaseViewModel<SearchViewState, SearchAction, Unit>() {

    override val _state: MutableStateFlow<SearchViewState> = MutableStateFlow(getInitialValue())
    override val _effect: MutableSharedFlow<Unit> = MutableSharedFlow()

    override val state: StateFlow<SearchViewState> = _state.asStateFlow()
    override val effect: SharedFlow<Unit> = _effect.asSharedFlow()

    init {
        initSearchQueryFlow()
    }

    override fun processAction(action: SearchAction) {
        when(action) {
            is SearchAction.OnSearch -> updateSearchQuery(action.query)
        }
    }

    private fun updateSearchQuery(movieName: String) {
        val query = getValue().query.copy(movieName = movieName)
        setState(getValue().copy(query = query))
    }

    private fun getInitialValue(): SearchViewState {
        return SearchViewState.getInitialState()
    }

    @OptIn(FlowPreview::class)
    private fun initSearchQueryFlow() {
        viewModelScope.launch(dispatcher) {
            state.distinctUntilChanged { old, new ->
                    old.query == new.query
                }.filter { searchState ->
                    searchState.query.movieName.isNotEmpty()
                }.debounce(500L)
                .onEach { setLoadingState() }
                .map { it.query }
                .collectLatest {
                    val resource = searchUseCase.searchMovies(it)
                    resource.fold(
                        onSuccess = { movies ->
                            if (movies.results.isNotEmpty()) {
                                setMovieSearched(movies)
                            } else {

                            }
                        },
                        onError = { error ->
                            setErrorState(error)
                        }
                    )
                }
        }
    }

    private fun setErrorState(error: String) {
        setState(getValue().copy(isLoading = false, error = error, searchedMovies = null))
    }

    private fun setMovieSearched(movies: MovieList) {
        setState(getValue().copy(isLoading = false, searchedMovies = movies, error = null))
    }

    private fun setLoadingState() {
        setState(getValue().copy(isLoading = true))
    }
}


sealed class SearchAction {
    data class OnSearch(val query: String): SearchAction()
}
data class SearchViewState(
    val isLoading: Boolean,
    val searchedMovies: MovieList?,
    val query: MovieSearchQuery,
    val error: String?,
) {

    fun noMoviesFound(): Boolean {
        if (searchedMovies == null) {
            // search not started
            return false
        }
        return searchedMovies.results.isEmpty() && !isLoading
    }

    companion object {
        fun getInitialState() : SearchViewState {
            return SearchViewState(
                isLoading = true,
                searchedMovies = null,
                query = MovieSearchQuery(""),
                error = null,
            )
        }
    }
}