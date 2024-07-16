package com.rodcollab.mymarvelcomics.core.data

import android.util.Log
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.model.toEntity
import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.database.FakeComicsDao
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.network.mockservice.DummyService
import com.rodcollab.mymarvelcomics.core.network.model.ComicDataWrapper
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import com.rodcollab.mymarvelcomics.core.utils.safeCallback
import retrofit2.Response

class FakeComicsRepository(
    private val favoritesDao: FakeComicsDao,
    private val remoteService: DummyService
) : ComicsRepository {
    override suspend fun fetchComics(onResult: (ResultOf<List<Comic>>) -> Unit) {
        safeCallback(
            callback = {
                remoteService.comics(0)
            }, onResult = { resultOf ->
                when (resultOf) {
                    is ResultOf.Failure -> onResult(resultOf)
                    is ResultOf.Success -> handleComicsResponse(resultOf.value, onResult)
                }
            })
    }

    fun handleComicsResponse(
        response: Response<ComicDataWrapper>,
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

    fun getComicsFromWrapper(response: Response<ComicDataWrapper>) =
        response.body()?.data?.results?.map {
            it.toComic()
        } ?: emptyList()

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
        }, onResult = onResult)
    }

    override suspend fun deleteFavoriteComic(comicId: Int, onResult: (ResultOf<Int>) -> Unit) {
        safeCallback(callback = {
            favoritesDao.deleteFavoriteComic(comicId)
            R.string.deleted_successfully_from_your_favorites
        }, onResult)
    }
}