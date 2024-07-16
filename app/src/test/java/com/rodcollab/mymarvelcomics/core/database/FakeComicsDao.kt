package com.rodcollab.mymarvelcomics.core.database

import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.database.dao.FavoriteComicsDao
import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity

class FakeComicsDao : FavoriteComicsDao {

    private val favoriteComics = mutableListOf<FavoriteComicEntity>()

    override suspend fun fetchFavoriteComics(): List<FavoriteComicEntity> = favoriteComics

    override suspend fun readFavoriteComic(id: Int): FavoriteComicEntity {
        return try {
            favoriteComics.find { it.toComic().id == id }!!
        } catch (e: Exception) {
            throw Throwable(e.message, cause = e)
        }
    }

    override suspend fun deleteFavoriteComic(id: Int) {
        try {
            favoriteComics.remove(favoriteComics.find { it.toComic().id == id }!!)
        } catch (e: Exception) {
            throw Throwable(e.message, cause = e)
        }
    }

    override suspend fun addFavoriteComic(comic: FavoriteComicEntity) {
        if (favoriteComics.find { it.toComic().id == comic.id } != null) {
            throw Exception("This primary key is already added to the table")
        }
        favoriteComics.add(comic)
    }

    fun clear() {
        favoriteComics.clear()
    }

}