package com.rodcollab.mymarvelcomics.core.network.model

data class ResponseContainer<T>(
    val code: Int,
    val data: ResponseData<T>,
    val status: String,
)