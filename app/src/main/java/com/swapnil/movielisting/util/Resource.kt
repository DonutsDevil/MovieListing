package com.swapnil.movielisting.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}

fun <T> Resource<T>.fold(
    onSuccess: (T) -> Unit = {},
    onError: (String) -> Unit = {},
) {
    when (this) {
        is Resource.Success -> onSuccess(data!!)
        is Resource.Error -> onError(message!!)
        is Resource.Loading -> {}
    }
}
