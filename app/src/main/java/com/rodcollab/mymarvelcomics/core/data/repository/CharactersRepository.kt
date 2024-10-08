package com.rodcollab.mymarvelcomics.core.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.rodcollab.mymarvelcomics.core.data.model.toExternal
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ResponseContainer
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import com.rodcollab.mymarvelcomics.core.utils.safeCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

interface CharactersRepository {
    fun getCharacters(pageSize: Int): Pager<Int, CharacterEntity>

    suspend fun getCharacterDetails(
        characterId: Int,
        onResult: (ResultOf<CharacterExternal>) -> Unit,
    )

    fun getPagingCharsByComic(pageSize: Int, charId: Int): Pager<Int, CharacterEntity>
}

class CharactersRepositoryImpl @Inject constructor(
    private val transactionProvider: TransactionProvider,
    private val charactersDao: CharactersDao,
    private val remoteService: MarvelApi,
) : CharactersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(pageSize: Int) = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = CharactersRemoteMediator(
            transactionProvider = transactionProvider,
            charactersDao = charactersDao,
            remoteService = remoteService
        )
    ) {
        charactersDao.charactersPagingSource()
    }

    override suspend fun getCharacterDetails(
        characterId: Int,
        onResult: (ResultOf<CharacterExternal>) -> Unit,
    ) {
        val characterDetails = withContext(Dispatchers.IO) {
            charactersDao.characterById(characterId)
        }
        characterDetails?.let { characterExternal ->

            onResult(ResultOf.Success(characterExternal.toExternal()))
        } ?: run {
            safeCallback(
                callback = {
                    remoteService.getCharacterDetails(characterId)
                }, onResult = { resultOf ->
                    when (resultOf) {
                        is ResultOf.Failure -> onResult(resultOf)
                        is ResultOf.Success -> handleCharactersResponse(resultOf.value, onResult)
                    }
                })
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingCharsByComic(pageSize: Int, comicId: Int): Pager<Int, CharacterEntity> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = CharactersRemoteMediator(
                comicId,
                transactionProvider,
                charactersDao,
                remoteService
            )
        ) {
            charactersDao.charactersPagingSource()
        }

    private fun handleCharactersResponse(
        response: Response<ResponseContainer<CharacterNetwork>>,
        onResult: (ResultOf<CharacterExternal>) -> Unit,
    ) {
        if (response.isSuccessful) {
            val result = try {
                val comics = getCharactersFromWrapper(response)
                Log.d("FECH_CHARACTERS_REPOSITORY", comics.toString())
                ResultOf.Success(comics)
            } catch (e: Exception) {
                Log.e("FECH_CHARACTERS_REPOSITORY", e.message.toString())
                ResultOf.Failure(message = e.message, throwable = e)
            }
            onResult(result)
        } else {
            onResult(ResultOf.Failure(message = response.message(), throwable = null))
        }
    }

    private fun getCharactersFromWrapper(response: Response<ResponseContainer<CharacterNetwork>>): CharacterExternal =
        response.body()?.data?.results!![0].toExternal()
}