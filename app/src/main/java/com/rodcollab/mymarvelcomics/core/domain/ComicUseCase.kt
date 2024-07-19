package com.rodcollab.mymarvelcomics.core.domain

import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComicUseCaseIml (
    private val comicsRepository: ComicsRepository,
) : ComicUseCase {

    override suspend fun invoke(
        comicId: Int,
        onResultOf: (ResultOf<Comic>) -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            comicsRepository.readFavoriteComic(comicId){ result ->
                when(result) {
                    is ResultOf.Success -> {
                        onResultOf(result)
                    }
                    else -> {
                        launch(Dispatchers.IO) {
                            comicsRepository.getComicDetails(comicId, onResultOf)
                        }
                    }
                }
            }
        }
    }

}

interface ComicUseCase {
    suspend operator fun invoke(comicId: Int, onResultOf: (ResultOf<Comic>) -> Unit)
}