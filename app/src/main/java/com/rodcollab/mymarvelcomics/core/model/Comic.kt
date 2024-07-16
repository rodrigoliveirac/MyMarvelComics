package com.rodcollab.mymarvelcomics.core.model

import com.rodcollab.mymarvelcomics.core.network.model.CharacterList
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import com.rodcollab.mymarvelcomics.core.network.model.Image
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

data class Comic(
    val characters: CharacterList,
    val collections: List<ContentSummary>,
    val description: String?,
    val id: Int,
    val isFavorite: Boolean,
    val images: List<Image?>,
    val pageCount: Int?,
    val resourceURI: String?,
    val thumbnail: Thumbnail?,
    val title: String?,
) {
    val urlImage = thumbnail?.let { thumbnail ->  "${thumbnail.path}.${thumbnail.extension}" }
    val urlImages = images.map { img -> img?.let { "${it.path}.${it.extension}" } }
}
