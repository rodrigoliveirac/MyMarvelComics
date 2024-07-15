package com.rodcollab.mymarvelcomics.core.network.model

data class ComicNetwork(
    val characters: CharacterList,
    val collections: List<ContentSummary>,
    val description: String,
    val id: Int,
    val images: List<Image>,
    val pageCount: Int,
    val resourceURI: String,
    val thumbnail: Thumbnail,
    val title: String,
) {
    val urlImage = "${thumbnail.path}.${thumbnail.extension}"
    val urlImages = images.map { img -> "${img.path}.${img.extension}" }
}