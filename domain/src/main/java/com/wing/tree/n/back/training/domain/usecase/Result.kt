package com.wing.tree.n.back.training.domain.usecase

sealed class Result<out R: Any> {
    data class Success<out T: Any>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

inline fun <R: Any, T: Any> Result<T>.map(transform: (T) -> R): Result<R> {
    return when(this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(throwable)
        is Result.Loading -> Result.Loading
    }
}