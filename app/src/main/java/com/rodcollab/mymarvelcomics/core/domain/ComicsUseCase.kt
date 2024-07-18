package com.rodcollab.mymarvelcomics.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ComicsUseCaseImpl (private val comicsRepository: ComicsRepository):
    ComicsUseCase {
    override fun invoke(): Flow<PagingData<Comic>> {
        return comicsRepository.getPagingComics(15).flow.map { it.map { it.toComic() } }
    }
}

interface ComicsUseCase {
    operator fun invoke() : Flow<PagingData<Comic>>
}