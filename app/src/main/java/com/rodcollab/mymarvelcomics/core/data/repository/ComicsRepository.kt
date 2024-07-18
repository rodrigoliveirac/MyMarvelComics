package com.rodcollab.mymarvelcomics.core.data.repository

import androidx.paging.Pager
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import com.rodcollab.mymarvelcomics.core.model.Comic

interface ComicsRepository {
    suspend fun fetchComics(onResult: (ResultOf<List<Comic>>) -> Unit)
    suspend fun fetchFavoriteComics(onResult: (ResultOf<List<Comic>>) -> Unit)
    suspend fun readFavoriteComic(comicId: Int, onResult: (ResultOf<Comic>) -> Unit)
    suspend fun addFavoriteComic(comic: Comic, onResult: (ResultOf<Int>) -> Unit)
    suspend fun deleteFavoriteComic(comicId: Int, onResult: (ResultOf<Int>) -> Unit)

    suspend fun readComic(comicId: Int, onResult: (ResultOf<Comic>) -> Unit)

    fun getPagingComics(pageSize: Int, comicId: Int) : Pager<Int, ComicEntity>

    fun getPagingComicsByCharId(pageSize: Int, charId: Int) : Pager<Int, ComicEntity>


}