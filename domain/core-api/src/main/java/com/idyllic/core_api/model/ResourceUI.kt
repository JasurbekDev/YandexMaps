package com.idyllic.core_api.model

@ResourceDSL
sealed class ResourceUI<out T> {
    data class Success<T>(val data: T, val statusCode: Int = 200) : ResourceUI<T>()
    data class Error(val error: Throwable, val statusCode: String = "") : ResourceUI<Nothing>()
}