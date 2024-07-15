package com.rodcollab.mymarvelcomics.core.network.model

data class ComicData(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<ComicNetwork>,
    val total: Int
)