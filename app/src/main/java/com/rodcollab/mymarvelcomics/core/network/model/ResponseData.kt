package com.rodcollab.mymarvelcomics.core.network.model

data class ResponseData<T>(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<T>,
    val total: Int
)