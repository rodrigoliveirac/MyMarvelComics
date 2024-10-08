package com.rodcollab.mymarvelcomics.core.network.model

data class ResourceList(
    val available: Int,
    val collectionURI: String,
    val items: List<ContentSummary>,
)