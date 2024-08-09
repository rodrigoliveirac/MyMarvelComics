package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.ComicsDao
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ComicsRemoteMediator(
    private val charId: Int? = null,
    private val transactionProvider: TransactionProvider,
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

            val offset = when(loadType) {
                LoadType.REFRESH -> currentPage
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        currentPage += state.config.pageSize
                        currentPage
                    } else {
                        currentPage += state.config.pageSize
                        currentPage
                    }
                }
            }

            val response = if (charId != null) {
                remoteService.getComicsByChar(
                    characterId = charId,
                    offset = offset,
                    limit = state.config.pageSize,
                )
            } else {
                remoteService.getComics(offset = offset, limit = state.config.pageSize)
            }

            val comics = response.body()?.data?.results?.map { comicNetwork ->

                ComicEntity(
                    id = comicNetwork.id,
                    title = comicNetwork.title,
                    description = comicNetwork.description,
                    thumbnail = comicNetwork.thumbnail,
                    resourceURI = comicNetwork.resourceURI,
                    pageCount = comicNetwork.pageCount,
                )
            } ?: emptyList()

            transactionProvider.runAsTransaction {
                if (loadType == LoadType.REFRESH) {
                    comicsDao.clearAll()
                }
                comicsDao.upsertAll(comics)
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
}
