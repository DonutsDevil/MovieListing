package com.swapnil.movielisting.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.swapnil.movielisting.di.IoDispatcher
import com.swapnil.movielisting.domain.model.preview.MoviePreview
import com.swapnil.movielisting.domain.usecase.preview.MoviePreviewUseCase
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
class MoviePreviewViewModel @Inject constructor(
    private val moviePreviewUseCase: MoviePreviewUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): BaseViewModel<PreviewState, PreviewAction, Unit>() {
    override val _state: MutableStateFlow<PreviewState> = MutableStateFlow(getInitialState())
    override val _effect: MutableSharedFlow<Unit> = MutableSharedFlow()

    override val state: StateFlow<PreviewState> = _state.asStateFlow()
    override val effect: SharedFlow<Unit> = _effect.asSharedFlow()

    override fun processAction(action: PreviewAction) {
        when(action) {
            is PreviewAction.PreviewSelected -> getSelectedPreview(action.selectedMovieId)
        }
    }

    private fun getInitialState(): PreviewState {
        return PreviewState.getInitialState()
    }

    private fun getSelectedPreview(selectedMovieId: Int) {
        viewModelScope.launch(dispatcher) {
            setLoadingState()
            val resource = moviePreviewUseCase.getPreview(selectedMovieId)
            resource.fold(
                onSuccess = { moviePreview ->
                    setMovieLoadedState(moviePreview)
                },
                onError = { error ->
                    setErrorState(error)
                }
            )
        }
    }

    private fun setLoadingState() {
        setState(getValue().copy(isLoading = true))
    }

    private fun setErrorState(error: String) {
        setState(getValue().copy(isLoading = false, error = error))
    }

    private fun setMovieLoadedState(movie: MoviePreview) {
        setState(getValue().copy(isLoading = false, movie = movie))
    }

}

sealed interface PreviewAction {
    data class PreviewSelected(val selectedMovieId: Int): PreviewAction
}


data class PreviewState(
    val isLoading: Boolean,
    val movie: MoviePreview?,
    val error: String?
) {
    companion object {
        fun getInitialState(): PreviewState {
            return PreviewState(
                isLoading = false,
                movie = null,
                error = null
            )
        }
    }
}