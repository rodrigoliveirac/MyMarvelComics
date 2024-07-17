package com.rodcollab.mymarvelcomics.core.network.model


data class CharacterNetwork(
    val id: Int,
    val name: String,
    val description: String?,
    val thumbnail: Image?,
    val comics: ResourceList?,
    val resourceURI: String
)