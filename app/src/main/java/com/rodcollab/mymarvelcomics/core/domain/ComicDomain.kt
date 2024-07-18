package com.rodcollab.mymarvelcomics.core.domain

data class ComicDomain(
    val comics: ComicsUseCase,
    val comic: ComicUseCase,
    val characters: CharactersByComicUseCase
)