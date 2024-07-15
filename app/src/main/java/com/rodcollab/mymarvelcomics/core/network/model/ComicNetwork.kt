package com.rodcollab.mymarvelcomics.core.network.model

data class ComicNetwork(
    val characters: CharacterList,
    val collections: List<ContentSummary>,
    val description: String,
    val id: Int,
    val images: List<Image>,
    val pageCount: Int,
    val resourceURI: String,
    val textObjects: List<TextObject>,
    val thumbnail: Thumbnail,
    val title: String,
    val urls: List<Url>,
    val variantDescription: String,
    val variants: List<Variant>
) {
    val urlImage = "${thumbnail.path}.${thumbnail.extension}"
    val urlImages = images.map { img -> "${img.path}.${img.extension}" }
}