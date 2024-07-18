package com.rodcollab.mymarvelcomics.core.domain

import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.utils.ResultOf

class ComicUseCaseIml (
    private val comicsRepository: ComicsRepository,
) : ComicUseCase {

    override suspend fun invoke(
        comicId: Int,
        onResultOf: (ResultOf<Comic>) -> Unit,
    ) = comicsRepository.getComicDetails(comicId, onResultOf)

}

interface ComicUseCase {
    suspend operator fun invoke(comicId: Int, onResultOf: (ResultOf<Comic>) -> Unit)
}