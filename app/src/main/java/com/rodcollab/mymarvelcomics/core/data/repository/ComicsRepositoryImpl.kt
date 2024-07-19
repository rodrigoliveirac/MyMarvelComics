package com.rodcollab.mymarvelcomics.core.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.model.toEntity
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.ComicsDao
import com.rodcollab.mymarvelcomics.core.database.dao.FavoriteComicsDao
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.network.model.ResponseContainer
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import com.rodcollab.mymarvelcomics.core.utils.safeCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ComicsRepositoryImpl @Inject constructor(
    private val transactionProvider: TransactionProvider,
    private val comicsDao: ComicsDao,
    private val favoritesDao: FavoriteComicsDao,
    private val remoteService: MarvelApi,
) : ComicsRepository {

    override suspend fun fetchComics(onResult: (ResultOf<List<Comic>>) -> Unit) {
        safeCallback(
            callback = {
                remoteService.getComics(0,0)
            }, onResult = { resultOf ->
                when (resultOf) {
                    is ResultOf.Failure -> onResult(resultOf)
                    is ResultOf.Success -> handleComicsResponse(resultOf.value, onResult)
                }
            })
    }

    private fun handleComicsResponse(
        response: Response<ResponseContainer<ComicNetwork>>,
        onResult: (ResultOf<List<Comic>>) -> Unit,
    ) {
        if (response.isSuccessful) {
            val result = try {
                val comics = getComicsFromWrapper(response)
                Log.d("FECH_COMICS_REPOSITORY", comics.toString())
                ResultOf.Success(comics)
            } catch (e: Exception) {
                Log.e("FECH_COMICS_REPOSITORY", e.message.toString())
                ResultOf.Failure(message = e.message, throwable = e)
            }
            onResult(result)
        } else {
            onResult(ResultOf.Failure(message = response.message(), throwable = null))
        }
    }

    private fun getComicsFromWrapper(response: Response<ResponseContainer<ComicNetwork>>) =
        response.body()?.data?.results?.map { it.toComic() } ?: emptyList()

    private fun getComicFromWrapper(response: Response<ResponseContainer<ComicNetwork>>) =
        response.body()?.data?.results!![0].toComic()

    override suspend fun fetchFavoriteComics(onResult: (ResultOf<List<Comic>>) -> Unit) {
        safeCallback(callback = { getComicsFromDb() }, onResult)
    }

    private suspend fun getComicsFromDb() = favoritesDao.fetchFavoriteComics().map { it.toComic() }

    override suspend fun readFavoriteComic(comicId: Int, onResult: (ResultOf<Comic>) -> Unit) {
        safeCallback(callback = { favoritesDao.readFavoriteComic(comicId).toComic() }, onResult)
    }

    override suspend fun addFavoriteComic(comic: Comic, onResult: (ResultOf<Int>) -> Unit) {
        safeCallback(callback = {
            favoritesDao.addFavoriteComic(comic.toEntity())
            R.string.added_succesfully_to_your_favorites
        }, onResult)
    }

    override suspend fun deleteFavoriteComic(comicId: Int, onResult: (ResultOf<Int>) -> Unit) {
        safeCallback(callback = {
            favoritesDao.deleteFavoriteComic(comicId)
            R.string.deleted_successfully_from_your_favorites
        }, onResult)
    }

    override suspend fun readComic(comicId: Int, onResult: (ResultOf<Comic>) -> Unit) {
        val comicDetails = withContext(Dispatchers.IO) {
            comicsDao.comicById(comicId)
        }
        onResult(ResultOf.Success(comicDetails.toComic())) ?: run {
            safeCallback(
                callback = {
                    remoteService.getComicDetails(comicId)
                }, onResult = { resultOf ->
                    when (resultOf) {
                        is ResultOf.Failure -> onResult(resultOf)
                        is ResultOf.Success -> handleComicResponse(resultOf.value, onResult)
                    }
                })
        }
    }

    private fun handleComicResponse(
        response: Response<ResponseContainer<ComicNetwork>>,
        onResult: (ResultOf<Comic>) -> Unit,
    ) {
        if (response.isSuccessful) {
            val result = try {
                val comics = getComicFromWrapper(response)
                Log.d("FECH_COMICS_REPOSITORY", comics.toString())
                ResultOf.Success(comics)
            } catch (e: Exception) {
                Log.e("FECH_COMICS_REPOSITORY", e.message.toString())
                ResultOf.Failure(message = e.message, throwable = e)
            }
            onResult(result)
        } else {
            onResult(ResultOf.Failure(message = response.message(), throwable = null))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingComics(pageSize: Int) = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = ComicsRemoteMediator(transactionProvider = transactionProvider, comicsDao = comicsDao, remoteService = remoteService)
    ) {
        comicsDao.comicsPagingSource()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingComicsByCharId(
        pageSize: Int,
        charId: Int,
    ) = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = ComicsRemoteMediator(charId, transactionProvider, comicsDao, remoteService)
    ) {
        comicsDao.comicsPagingSource()
    }

    override suspend fun getComicDetails(comicId: Int, onResult: (ResultOf<Comic>) -> Unit) {
        withContext(Dispatchers.IO) {
            val comic = comicsDao.comicById(comicId)
            onResult(ResultOf.Success(comic.toComic())) ?: run {
                safeCallback(
                    callback = {
                        remoteService.getComicDetails(comicId)
                    }, onResult = { resultOf ->
                        when (resultOf) {
                            is ResultOf.Failure -> onResult(resultOf)
                            is ResultOf.Success -> handleComicResponse(resultOf.value, onResult)
                        }
                    })
            }
        }

    }
}