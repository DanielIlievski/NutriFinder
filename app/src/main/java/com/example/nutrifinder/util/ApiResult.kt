package com.example.nutrifinder.util

import io.ktor.http.HttpStatusCode

sealed interface ApiResult<out T> {
    data class Success<out T>(val data: T): ApiResult<T>

    sealed interface Failure<out T> : ApiResult<T> {
        data class Error(val statusCode: HttpStatusCode, val message: String): Failure<Nothing>

        data class Exception(val message: String): Failure<Nothing>
    }
}