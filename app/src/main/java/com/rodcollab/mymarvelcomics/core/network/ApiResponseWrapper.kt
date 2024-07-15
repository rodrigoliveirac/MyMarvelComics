package com.rodcollab.mymarvelcomics.core.network

sealed class ApiResponseWrapper<T> {
    data class ApiSuccessResponse<T>(val data: T) : ApiResponseWrapper<T>()
    data class ApiErrorResponse<T>(val data: T) : ApiResponseWrapper<T>()
}