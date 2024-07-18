package com.rodcollab.mymarvelcomics.core.domain

import com.rodcollab.mymarvelcomics.core.data.repository.CharactersRepository
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.utils.ResultOf

class CharacterUseCaseImpl (
    private val charactersRepository: CharactersRepository,
) : CharacterUseCase {

    override suspend fun invoke(
        characterId: Int,
        onResultOf: (ResultOf<CharacterExternal>) -> Unit,
    ) = charactersRepository.getCharacterDetails(characterId, onResultOf)

}

interface CharacterUseCase {
    suspend operator fun invoke(characterId: Int, onResultOf: (ResultOf<CharacterExternal>) -> Unit)
}