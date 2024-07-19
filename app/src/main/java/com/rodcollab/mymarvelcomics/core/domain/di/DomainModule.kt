package com.rodcollab.mymarvelcomics.core.domain.di

import com.rodcollab.mymarvelcomics.core.data.repository.CharactersRepository
import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.domain.AddOrRemoveFromFavoritesImpl
import com.rodcollab.mymarvelcomics.core.domain.CharacterUseCase
import com.rodcollab.mymarvelcomics.core.domain.CharacterUseCaseImpl
import com.rodcollab.mymarvelcomics.core.domain.CharactersByComicUseCaseImpl
import com.rodcollab.mymarvelcomics.core.domain.CharactersUseCase
import com.rodcollab.mymarvelcomics.core.domain.CharactersUseCaseImpl
import com.rodcollab.mymarvelcomics.core.domain.ComicDomain
import com.rodcollab.mymarvelcomics.core.domain.ComicUseCaseIml
import com.rodcollab.mymarvelcomics.core.domain.ComicsUseCaseImpl
import com.rodcollab.mymarvelcomics.core.domain.FavoriteComicsImpl
import com.rodcollab.mymarvelcomics.core.domain.GetComicsByCharId
import com.rodcollab.mymarvelcomics.core.domain.GetComicsByCharIdImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object DomainModule {

    @ViewModelScoped
    @Provides
    fun providesCharactersDomain(
        comicsRepository: ComicsRepository,
        charactersRepository: CharactersRepository,

    ): CharacterDomain {
        return CharacterDomain(
            characters = CharactersUseCaseImpl(charactersRepository),
            character = CharacterUseCaseImpl(charactersRepository),
            comics = GetComicsByCharIdImpl(comicsRepository)
        )
    }

    @ViewModelScoped
    @Provides
    fun providesComicsDomain(
        comicsRepository: ComicsRepository,
        charactersRepository: CharactersRepository,

        ): ComicDomain {
        return ComicDomain(
            characters = CharactersByComicUseCaseImpl(charactersRepository),
            comics = ComicsUseCaseImpl(comicsRepository),
            comic = ComicUseCaseIml(comicsRepository),
            addOrRemoveFromFavorites = AddOrRemoveFromFavoritesImpl(comicsRepository),
            favorites = FavoriteComicsImpl(comicsRepository)
        )
    }
}

data class CharacterDomain(
    val characters : CharactersUseCase,
    val character: CharacterUseCase,
    val comics: GetComicsByCharId
)