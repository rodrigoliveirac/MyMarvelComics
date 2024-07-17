package com.rodcollab.mymarvelcomics.core.data

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rodcollab.mymarvelcomics.MyMarvelComicsApp
import com.rodcollab.mymarvelcomics.core.characters
import com.rodcollab.mymarvelcomics.core.data.repository.CharactersRemoteMediator
import com.rodcollab.mymarvelcomics.core.database.AppDatabase
import com.rodcollab.mymarvelcomics.core.database.DefaultTransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.dao.ComicsDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.network.FakeMarvelApi
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = MyMarvelComicsApp::class)
class CharactersRemoteMediatorTest  {

    private lateinit var context: Context
    private lateinit var service: FakeMarvelApi
    private lateinit var db: AppDatabase
    private lateinit var charactersDao: CharactersDao
    private lateinit var comicsDao: ComicsDao

    @Before
    fun setup() = runTest {
        context = ApplicationProvider.getApplicationContext()

        val app = ApplicationProvider.getApplicationContext<MyMarvelComicsApp>()
        context = app.applicationContext
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        charactersDao = db.charactersDao()
        comicsDao = db.comicsDao()

        service = FakeMarvelApi()

    }

    @After
    fun tearDown() {
        db.clearAllTables()
        service.failureMsg = null
        service.clearCharacters()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {

        service = service.apply {
            characters.forEach { character -> addCharacter(character)
            }
        }

        val remoteMediator = CharactersRemoteMediator(
            DefaultTransactionProvider(db),
            charactersDao,
            comicsDao,
            service
        )
        val pagingState = PagingState<Int, CharacterEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        // To test endOfPaginationReached, don't set up the mockApi to return post
        // data here.

        val remoteMediator = CharactersRemoteMediator(
            DefaultTransactionProvider(db),
            charactersDao,
            comicsDao,
            service
        )
        val pagingState = PagingState<Int, CharacterEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        // Set up failure message to throw exception from the mock API.
        service.failureMsg = "Throw test failure"
        val remoteMediator = CharactersRemoteMediator(
            DefaultTransactionProvider(db),
            charactersDao,
            comicsDao,
            service
        )
        val pagingState = PagingState<Int, CharacterEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue (result is RemoteMediator.MediatorResult.Error )
    }

}