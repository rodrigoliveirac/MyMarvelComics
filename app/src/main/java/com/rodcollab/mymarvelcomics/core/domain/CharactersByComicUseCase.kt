package com.rodcollab.mymarvelcomics.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.rodcollab.mymarvelcomics.core.data.model.toExternal
import com.rodcollab.mymarvelcomics.core.data.repository.CharactersRepository
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class CharactersByComicUseCaseImpl (private val characters: CharactersRepository) : CharactersByComicUseCase {
    override fun invoke(comicId: Int): Flow<PagingData<CharacterExternal>> {
        return characters.getPagingCharsByComic(15, comicId).flow.map { it.map { it.toExternal() } }
    }
}

interface CharactersByComicUseCase {
    operator fun invoke(comicId: Int): Flow<PagingData<CharacterExternal>>
}