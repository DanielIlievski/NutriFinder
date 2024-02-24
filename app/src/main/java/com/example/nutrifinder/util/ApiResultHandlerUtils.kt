package com.example.nutrifinder.util

inline fun <T> ApiResult<T>.onSuccess(
    crossinline onResult: ApiResult.Success<T>.() -> Unit
): ApiResult<T> {
    if (this is ApiResult.Success) {
        onResult(this)
    }

    return this
}

suspend inline fun <T> ApiResult<T>.suspendOnSuccess(
    crossinline onResult: suspend ApiResult.Success<T>.() -> Unit
): ApiResult<T> {
    if (this is ApiResult.Success) {
        onResult(this)
    }

    return this
}

inline fun <T> ApiResult<T>.onError(
    crossinline onResult: ApiResult.Failure.Error.() -> Unit
): ApiResult<T> {
    if (this is ApiResult.Failure.Error) {
        onResult(this)
    }

    return this
}

suspend inline fun <T> ApiResult<T>.suspendOnError(
    crossinline onResult: suspend ApiResult.Failure.Error.() -> Unit
): ApiResult<T> {
    if (this is ApiResult.Failure.Error) {
        onResult(this)
    }

    return this
}

inline fun <T> ApiResult<T>.onException(
    crossinline onResult: ApiResult.Failure.Exception.() -> Unit
): ApiResult<T> {
    if (this is ApiResult.Failure.Exception) {
        onResult(this)
    }

    return this
}

suspend inline fun <T> ApiResult<T>.suspendOnException(
    crossinline onResult: suspend ApiResult.Failure.Exception.() -> Unit
): ApiResult<T> {
    if (this is ApiResult.Failure.Exception) {
        onResult(this)
    }

    return this
}