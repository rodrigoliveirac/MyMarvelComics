package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rodcollab.mymarvelcomics.core.data.model.toEntity
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.dao.ComicsDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import com.rodcollab.mymarvelcomics.core.network.service.lastPath
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ComicsRemoteMediator(
    private val transactionProvider: TransactionProvider,
    private val charactersDao: CharactersDao,
    private val comicsDao: ComicsDao,
    private val remoteService: MarvelApi,
) : RemoteMediator<Int, ComicEntity>() {

    private var currentPage: Int = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ComicEntity>,
    ): MediatorResult {
        try {

            val response = remoteService.getComics(
                offset = currentPage * state.config.pageSize,
                limit = state.config.pageSize,
            )

            val comics = response.body()?.data?.results?.map { comicNetwork ->

                val characters = getEntitiesFromContentSummary(contentSummary = comicNetwork.characters?.items ?: emptyList())

                charactersDao.upsertAll(characters.values.toList())

                ComicEntity(
                    remoteId = comicNetwork.id,
                    title = comicNetwork.title,
                    description = comicNetwork.description,
                    thumbnail = comicNetwork.thumbnail,
                    characters = characters.keys.toList(),
                    images = comicNetwork.images,
                    collections = comicNetwork.collections,
                    resourceURI = comicNetwork.resourceURI,
                    pageCount = comicNetwork.pageCount,
                    resourceList = comicNetwork.characters
                )
            } ?: emptyList()

            transactionProvider.runAsTransaction {
                if (loadType == LoadType.REFRESH) {
                    comicsDao.clearAll()
                    charactersDao.clearAll()
                }

                comicsDao.upsertAll(comics)
            }

            if (currentPage * state.config.pageSize >= response.body()?.data?.count!!) {
                currentPage++
            }

            return MediatorResult.Success(endOfPaginationReached = comics.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getEntitiesFromContentSummary(contentSummary: List<ContentSummary>): Map<Int, CharacterEntity> {

        val hmComics = hashMapOf<Int, CharacterEntity>()

        contentSummary.forEach { resourceList ->

            val remoteId = resourceList.resourceURI.lastPath().toInt()

            mapOf(remoteId to getItemFromService<ComicNetwork>(remoteId, resourceList))
        }
        return hmComics
    }

    private suspend fun <T>getItemFromService(
        remoteId: Int,
        resourceList: ContentSummary,
    ): CharacterEntity? {
        val characterDetails = try {
             remoteService.getCharacterDetails(
                characterId = remoteId,
            ).body()?.data?.results?.get(0)?.toEntity()
        } catch (e: Exception) {
            CharacterEntity(
                remoteId = remoteId,
                name = resourceList.name,
                resourceURI = resourceList.resourceURI,
                description = null,
                thumbnail = null,
                comics = null
            )
        }
        return characterDetails
    }
}
