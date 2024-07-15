package com.rodcollab.mymarvelcomics.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeCallback(
    callback: suspend () -> T,
    onResult: (ResultOf<T>) -> Unit,
) {
    withContext(Dispatchers.IO) {
        try {
            onResult(ResultOf.Success(callback()))
        } catch (e: Exception) {
            onResult(ResultOf.Failure(message = e.message, throwable = e))
        }
    }
}