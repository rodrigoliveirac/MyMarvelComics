package com.rodcollab.mymarvelcomics.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.rodcollab.mymarvelcomics.core.data.model.toExternal
import com.rodcollab.mymarvelcomics.core.data.repository.CharactersRepository
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CharactersUseCaseImpl @Inject constructor(private val charactersRepository: CharactersRepository):
    CharactersUseCase {
    override fun invoke(): Flow<PagingData<CharacterExternal>> {
        return charactersRepository.getCharacters(15).flow.map { it.map { it.toExternal(emptyList()) } }
    }
}

interface CharactersUseCase {
    operator fun invoke() : Flow<PagingData<CharacterExternal>>
}