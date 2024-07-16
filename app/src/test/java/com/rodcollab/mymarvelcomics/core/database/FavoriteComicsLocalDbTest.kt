package com.rodcollab.mymarvelcomics.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rodcollab.mymarvelcomics.MyMarvelComicsApp
import com.rodcollab.mymarvelcomics.core.comicsFromNetwork
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.model.toEntity
import com.rodcollab.mymarvelcomics.core.database.dao.FavoriteComicsDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = MyMarvelComicsApp::class)
class FavoriteComicsLocalDbTest {

    private lateinit var database: AppDatabase
    private lateinit var favoriteComicsDao: FavoriteComicsDao
    private lateinit var context: Context

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<MyMarvelComicsApp>()
        context = app.applicationContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        favoriteComicsDao = database.favoriteComicsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun `When the user add a comic to the favorite table`() = runTest {

        val comics = comicsFromNetwork()

        val comicMarked = comics.random().toComic().toEntity()

        favoriteComicsDao.addFavoriteComic(comicMarked)

        val firstItemFromFavorite = favoriteComicsDao.fetchFavoriteComics()[0]

        assertThat(comicMarked.id, equalTo(firstItemFromFavorite.id))

    }

    @Test
    @Throws(Exception::class)
    fun `When the user remove the comic to the favorite table`() = runTest {

        val comics = comicsFromNetwork()

        val comicMarked = comics.random().toComic().toEntity()

        favoriteComicsDao.addFavoriteComic(comicMarked)

        val favoriteComic = favoriteComicsDao.readFavoriteComic(comicMarked.id)

        favoriteComicsDao.deleteFavoriteComic(favoriteComic.id)

        val favorites = favoriteComicsDao.fetchFavoriteComics()

        assertThat(favorites.isEmpty(), equalTo(true))
    }

    @Test
    @Throws(Exception::class)
    fun `When the user fetch all favorites`() = runTest {

        val comics = comicsFromNetwork()

        comics.forEach {
            favoriteComicsDao.addFavoriteComic(it.toComic().toEntity())
        }

        assertThat(favoriteComicsDao.fetchFavoriteComics().size, equalTo(comics.size))
    }

    @Test
    @Throws(Exception::class)
    fun `When the user fetch empty favorite list`() = runTest {
        assertThat(favoriteComicsDao.fetchFavoriteComics().size, equalTo(0))
    }

}