package com.rodcollab.mymarvelcomics.core.network.model

data class ComicNetwork(
    val description: String?,
    val id: Int,
    val pageCount: Int?,
    val resourceURI: String?,
    val thumbnail: Thumbnail? ,
    val title: String?,
)