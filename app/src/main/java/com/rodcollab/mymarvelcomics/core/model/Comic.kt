package com.rodcollab.mymarvelcomics.core.model

import com.rodcollab.mymarvelcomics.core.network.model.CharacterList
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary

data class Comic(
    val characters: CharacterList,
    val collections: List<ContentSummary>,
    val description: String,
    val id: Int,
    val images: List<String>,
    val pageCount: Int,
    val resourceURI: String,
    val thumbnail: String,
    val title: String,
)
