package com.rodcollab.mymarvelcomics.core.domain

import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.utils.ResultOf

data class ComicDomain(
    val comics: ComicsUseCase,
    val comic: ComicUseCase,
    val characters: CharactersByComicUseCase,
    val addOrRemoveFromFavorites: AddOrRemoveFromFavorites,
    val favorites: FavoriteComics
)

class FavoriteComicsImpl (private val comicsRepository: ComicsRepository):
    FavoriteComics {
    override suspend fun invoke(onResult: (ResultOf<List<Comic>>) -> Unit) {
        return comicsRepository.fetchFavoriteComics(onResult)
    }
}

interface FavoriteComics {
    suspend operator fun invoke(onResult: (ResultOf<List<Comic>>) -> Unit)
}