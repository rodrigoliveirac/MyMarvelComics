package com.rodcollab.mymarvelcomics.core.domain

import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.utils.ResultOf

class AddOrRemoveFromFavoritesImpl(
    private val favoriteComics: ComicsRepository,
) : AddOrRemoveFromFavorites {
    override suspend fun invoke(
        comicId: Int,
        model: Comic,
        onResult: (ResultOf<Int>) -> Unit,
    ) {
        if (model.isFavorite) {
            favoriteComics.deleteFavoriteComic(comicId, onResult)
        } else {
            favoriteComics.addFavoriteComic(model, onResult)
        }
    }
}

interface AddOrRemoveFromFavorites {
    suspend operator fun invoke(
        comicId: Int,
        model: Comic,
        onResult: (ResultOf<Int>) -> Unit,
    )
}