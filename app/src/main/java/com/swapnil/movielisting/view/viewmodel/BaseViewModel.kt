package com.swapnil.movielisting.view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<State, Action, Effect>: ViewModel() {
    protected abstract val _state: MutableStateFlow<State>
    protected abstract val _effect: MutableSharedFlow<Effect>
    abstract val state: StateFlow<State>
    abstract val effect: SharedFlow<Effect>

    abstract fun processAction(action: Action)

    protected fun setState(newState: State) {
        _state.value = newState
    }

    protected fun getValue(): State {
        return state.value
    }
}