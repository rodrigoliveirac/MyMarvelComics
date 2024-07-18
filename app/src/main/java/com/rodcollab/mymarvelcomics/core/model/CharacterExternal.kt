package com.rodcollab.mymarvelcomics.core.model

data class CharacterExternal(
    val id: Int,
    val name: String,
    val description: String?,
    val thumbnail: String?,
)