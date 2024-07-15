package com.rodcollab.mymarvelcomics.core.network.model

data class CharactersNetwork(
    val available: Int,
    val collectionURI: String,
    val items: List<CharacterSummary>,
    val returned: Int
)