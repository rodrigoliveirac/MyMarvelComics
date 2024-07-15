package com.rodcollab.mymarvelcomics.core.network.model

data class ComicNetwork(
    val characters: CharactersNetwork,
    val collections: List<Any>,
    val description: String,
    val id: Int,
    val images: List<Any>,
    val pageCount: Int,
    val resourceURI: String,
    val textObjects: List<Any>,
    val thumbnail: Thumbnail,
    val title: String,
    val urls: List<Url>,
    val variantDescription: String,
    val variants: List<Variant>
)