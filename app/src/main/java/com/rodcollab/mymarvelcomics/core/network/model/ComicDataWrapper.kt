package com.rodcollab.mymarvelcomics.core.network.model

data class ComicDataWrapper(
    val code: Int,
    val data: ComicData,
    val status: String
)