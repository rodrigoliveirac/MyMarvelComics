package com.rodcollab.mymarvelcomics.core.domain.di

import com.rodcollab.mymarvelcomics.core.data.repository.CharactersRepository
import com.rodcollab.mymarvelcomics.core.domain.CharactersUseCase
import com.rodcollab.mymarvelcomics.core.domain.CharactersUseCaseImpl
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
        charactersRepository: CharactersRepository,
    ): CharactersUseCase {
        return CharactersUseCaseImpl(charactersRepository)
    }
}