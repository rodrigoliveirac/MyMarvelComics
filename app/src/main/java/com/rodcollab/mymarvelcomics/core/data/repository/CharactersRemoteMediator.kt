package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
    private val comicId: Int? = null,
    private val transactionProvider: TransactionProvider,
    private val charactersDao: CharactersDao,
    private val remoteService: MarvelApi,
) : RemoteMediator<Int, CharacterEntity>() {

    private var currentPage: Int = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult {
        try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> currentPage
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        currentPage += state.config.pageSize
                        currentPage
                    } else {
                        currentPage += state.config.pageSize
                        currentPage
                    }
                }
            }

            val response = if (comicId != null) {
                remoteService.getCharactersByComicId(
                    comicId = comicId,
                    offset = loadKey,
                    limit = state.config.pageSize,
                )
            } else {
                remoteService.getCharacters(
                    offset = loadKey,
                    limit = state.config.pageSize,
                )
            }


            val characters = response.body()?.data?.results?.map { characterNetwork ->

                CharacterEntity(
                    id = characterNetwork.id,
                    name = characterNetwork.name,
                    description = characterNetwork.description,
                    thumbnail = "${characterNetwork.thumbnail?.path}.${characterNetwork.thumbnail?.extension}",
                    resourceURI = characterNetwork.resourceURI
                )
            } ?: emptyList()

            transactionProvider.runAsTransaction {
                if (loadType == LoadType.REFRESH) {
                    charactersDao.clearAll()
                }
                charactersDao.upsertAll(characters)
            }

            return MediatorResult.Success(endOfPaginationReached = characters.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

}