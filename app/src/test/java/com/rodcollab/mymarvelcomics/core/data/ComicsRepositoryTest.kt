package com.rodcollab.mymarvelcomics.core.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.MockServiceContent
import com.rodcollab.mymarvelcomics.core.comicsFromNetwork
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.database.FakeComicsDao
import com.rodcollab.mymarvelcomics.core.network.AndroidFileReader
import com.rodcollab.mymarvelcomics.core.network.mockservice.DummyService
import com.rodcollab.mymarvelcomics.core.network.mockservice.MockInterceptor
import com.rodcollab.mymarvelcomics.core.network.mockservice.WebServiceAbstraction
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import com.rodcollab.mymarvelcomics.core.utils.StatusCode
import com.rodcollab.mymarvelcomics.core.utils.getOkHttpClient
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ComicsRepositoryTest : WebServiceAbstraction<DummyService>() {

    private lateinit var fakeFavoriteComicsDao: FakeComicsDao
    private lateinit var comicsRepository: ComicsRepository
    private lateinit var service: DummyService
    private lateinit var context: Context
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() = runTest {

        context = ApplicationProvider.getApplicationContext<Context>()

        okHttpClient = getOkHttpClient(
            interceptor =
            MockInterceptor(
                code = StatusCode.OK.code,
                fileReader = AndroidFileReader(context),
                mockContent = MockServiceContent.COMICS
            )
        )
        service = createService(DummyService::class.java, okHttpClient)

        fakeFavoriteComicsDao = FakeComicsDao()

        comicsRepository = FakeComicsRepository(fakeFavoriteComicsDao, service)
    }

    @Test
    fun handleComicsResponseOK() = runTest {

        val response = service.comics(1)

        val repository = (comicsRepository as FakeComicsRepository)

        val comics = repository.getComicsFromWrapper(response)

        repository
            .handleComicsResponse(
                response, onResult = { resultOf ->
                    assertThat(resultOf, IsEqual.equalTo(ResultOf.Success(comics)))
                })
    }

    @Test
    fun handleComicsResponseIsNotOK() = runTest {

        setupAlternativeService(StatusCode.UNAUTHORIZED.code)

        val response = service.comics(1)

        val repository = (comicsRepository as FakeComicsRepository)

        repository
            .handleComicsResponse(
                response, onResult = { resultOf ->
                    assertThat(
                        resultOf,
                        IsEqual.equalTo(
                            ResultOf.Failure(
                                message = StatusCode.UNAUTHORIZED.message,
                                null
                            )
                        )
                    )
                })
    }

    private suspend fun ComicsRepositoryTest.setupAlternativeService(code: Int) {
        okHttpClient = getOkHttpClient(
            interceptor =
            MockInterceptor(
                code = code,
                fileReader = AndroidFileReader(context),
                mockContent = MockServiceContent.COMICS
            )
        )
        service = createService(DummyService::class.java, okHttpClient)
    }

    @Test
    fun `When the user fetch the Comics from Api`() = runTest {

        val response = service.comics(1)

        val comics = response.body()?.data?.results?.map { it.toComic() }

        comicsRepository.fetchComics {
            TestCase.assertTrue(it is ResultOf.Success)
            assertThat(it, IsEqual.equalTo(ResultOf.Success(comics)))
        }
    }

    @Test
    fun `When the user fetch the Comics from Api and get error`() = runTest {

        setupAlternativeService(StatusCode.GENERIC.code)

        comicsRepository = FakeComicsRepository(fakeFavoriteComicsDao, service)

        comicsRepository.fetchComics {
            TestCase.assertTrue(it is ResultOf.Failure)
            assertThat(
                it,
                IsEqual.equalTo(ResultOf.Failure(message = StatusCode.GENERIC.message, null))
            )
        }
    }

    @Test
    fun `When the user fetch the Favorite Comics EmptyList from local database`() = runTest {
        comicsRepository.fetchFavoriteComics {
            TestCase.assertTrue(it is ResultOf.Success)
            assertThat(it, IsEqual.equalTo(ResultOf.Success(emptyList())))
        }
    }

    @Test
    fun `When the system try to add a duplicate item in favorite and get error`() = runTest {

        val comic = comicsFromNetwork().random()

        comicsRepository.addFavoriteComic(comic.toComic(), onResult = { })

        comicsRepository.addFavoriteComic(comic.toComic(), onResult = { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Failure)
            assertThat(
                "This primary key is already added to the table",
                IsEqual.equalTo((resultOf as ResultOf.Failure).message)
            )
        })
    }

    @Test
    fun `When the user try to add a item successfully`() = runTest {

        val comic = comicsFromNetwork().random()

        comicsRepository.addFavoriteComic(comic.toComic(), onResult = { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
            assertThat(
                resultOf,
                IsEqual.equalTo(ResultOf.Success(R.string.added_succesfully_to_your_favorites))
            )
        })
    }

    @Test
    fun `When the user try to remove a comic from favorites successfully`() = runTest {

        val comic = comicsFromNetwork().random()

        comicsRepository.addFavoriteComic(comic.toComic(), onResult = { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
            assertThat(
                resultOf,
                IsEqual.equalTo(ResultOf.Success(R.string.added_succesfully_to_your_favorites))
            )
        })

        comicsRepository.deleteFavoriteComic(comicId = comic.id, onResult = { resultOf ->
            TestCase.assertTrue(resultOf is ResultOf.Success)
            assertThat(
                resultOf,
                IsEqual.equalTo(ResultOf.Success(R.string.deleted_successfully_from_your_favorites))
            )
        })
    }
}