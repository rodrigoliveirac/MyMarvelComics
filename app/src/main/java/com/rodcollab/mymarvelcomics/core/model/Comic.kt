package com.rodcollab.mymarvelcomics.core.model

import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import com.rodcollab.mymarvelcomics.core.network.model.Image
import com.rodcollab.mymarvelcomics.core.network.model.ResourceList
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

data class Comic(
    val description: String?,
    val id: Int,
    val isFavorite: Boolean,
    val pageCount: Int?,
    val resourceURI: String?,
    val thumbnail: Thumbnail?,
    val title: String?,
) {
    val urlImage = thumbnail?.let { thumbnail ->  "${thumbnail.path}.${thumbnail.extension}" }
}
