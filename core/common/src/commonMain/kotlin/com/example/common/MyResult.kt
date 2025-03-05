package com.example.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface MyResult<out T> {
    data class Success<T>(val data: T) : MyResult<T>
    data class Error(val exception: Throwable) : MyResult<Nothing>
    data object Loading : MyResult<Nothing>

    val success: Boolean
        get() = this is Success

    fun getOrNull(): T? = (this as? Success)?.data
    fun exceptionOrNull(): Throwable? = (this as? Error)?.exception
}

fun <T> Flow<T>.asResult(): Flow<MyResult<T>> = map<T, MyResult<T>> { MyResult.Success(it) }
    .onStart { emit(MyResult.Loading) }
    .catch { emit(MyResult.Error(it)) }