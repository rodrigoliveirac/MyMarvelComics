package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rodcollab.mymarvelcomics.core.data.model.toEntity
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import com.rodcollab.mymarvelcomics.core.network.service.lastPath
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
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

            val response = remoteService.getCharacters(
                offset = currentPage * state.config.pageSize,
                limit = state.config.pageSize,
            )

            val characters = response.body()?.data?.results?.map { characterNetwork ->

                val comics = getEntitiesFromContentSummary(contentSummary = characterNetwork.comics.items)

                CharacterEntity(
                    name = characterNetwork.name,
                    description = characterNetwork.description,
                    thumbnail = characterNetwork.thumbnail,
                    comics = comics
                )
            } ?: emptyList()

            transactionProvider.runAsTransaction {
                if (loadType == LoadType.REFRESH) {
                    charactersDao.clearAll()
                }

                charactersDao.upsertAll(characters)
            }

            if (currentPage * state.config.pageSize >= response.body()?.data?.count!!) {
                currentPage++
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

    private suspend fun getEntitiesFromContentSummary(contentSummary: List<ContentSummary>): List<ComicEntity?> {

        val comics = contentSummary.map { resourceList ->

            val remoteId = resourceList.resourceURI.lastPath().toInt()

            getItemFromService<ComicNetwork>(remoteId, resourceList)
        }
        return comics
    }

    private suspend fun <T>getItemFromService(
        remoteId: Int,
        resourceList: ContentSummary,
    ): ComicEntity? {
        val comicDetails = try {
            remoteService.getComicDetails(
                comicId = remoteId,
            ).body()?.data?.results?.get(0)?.toEntity(remoteId)
        } catch (e: Exception) {
            ComicNetwork(
                title = resourceList.name,
                resourceURI = resourceList.resourceURI,
                id = remoteId,
                collections = null,
                description = null,
                pageCount = null,
                characters = null,
                thumbnail = null,
                images = null
            ).toEntity(remoteId)
        }
        return comicDetails
    }
}