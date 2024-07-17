package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.PagingData
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import com.rodcollab.mymarvelcomics.core.model.Comic
import kotlinx.coroutines.flow.Flow

interface ComicsRepository {
    suspend fun fetchComics(onResult: (ResultOf<List<Comic>>) -> Unit)
    suspend fun fetchFavoriteComics(onResult: (ResultOf<List<Comic>>) -> Unit)
    suspend fun readFavoriteComic(comicId: Int, onResult: (ResultOf<Comic>) -> Unit)
    suspend fun addFavoriteComic(comic: Comic, onResult: (ResultOf<Int>) -> Unit)
    suspend fun deleteFavoriteComic(comicId: Int, onResult: (ResultOf<Int>) -> Unit)

    suspend fun readComic(comicId: Int, onResult: (ResultOf<Comic>) -> Unit)

    fun getPagingComics(pageSize: Int, comicId: Int) : Flow<PagingData<ComicEntity>>
}