package com.hedgehog.domain.wrapper

sealed class CaseResult<out T, out E> {
    data class Success<T>(val response: T) : CaseResult<T, Nothing>()
    data class Failure<E>(val reason: E) : CaseResult<Nothing, E>()
}