package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getCharacters(pageSize: Int, comicId: Int) : Flow<PagingData<CharacterEntity>>
}

class CharactersRepositoryImpl(
    private val transactionProvider: TransactionProvider,
    private val charactersDao: CharactersDao,
    private val remoteService: MarvelApi
) : CharactersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(pageSize: Int, comicId: Int) = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = CharactersRemoteMediator(transactionProvider,charactersDao, remoteService)
    ) {
        charactersDao.charactersPagingSource()
    }.flow
}