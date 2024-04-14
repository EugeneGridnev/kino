package ru.eugeneprojects.avitofilms

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.moviedescription.Country
import ru.eugeneprojects.avitofilms.data.models.moviedescription.Genre
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieCardInfo
import ru.eugeneprojects.avitofilms.data.models.moviedescription.Poster
import ru.eugeneprojects.avitofilms.data.models.moviedescription.Rating
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository
import ru.eugeneprojects.avitofilms.data.paging.MoviePagingSource

@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PagingTests {

    @Mock
    lateinit var kinopoiskRepository: KinopoiskRepository

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun httpErrorMoviesTest(): Unit = runBlocking {
        val moviePagingSource: MoviePagingSource =
            MoviePagingSource(kinopoiskRepository)

        whenever(kinopoiskRepository.getMovies(any(), any())).thenReturn(
            Response.error(
                500,
                "".toResponseBody()
            )
        )

        val result = moviePagingSource.load(
            PagingSource.LoadParams.Append(
                1,
                20,
                false
            )
        )

        assertInstanceOf(PagingSource.LoadResult.Error::class.java, result)

        val error = (result as PagingSource.LoadResult.Error).throwable

        assertInstanceOf(HttpException::class.java, error)

        val response = (error as HttpException).response()

        assertNotNull(response)
        assertEquals(500, response!!.code())
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test()
    fun httpExceptionMoviesTest(): Unit = runBlocking {
        val moviePagingSource: MoviePagingSource =
            MoviePagingSource(kinopoiskRepository)

        whenever(kinopoiskRepository.getMovies(any(), any())).thenThrow(OutOfMemoryError())

        assertThrows<OutOfMemoryError> {
            moviePagingSource.load(
                PagingSource.LoadParams.Append(
                    1,
                    20,
                    false
                )
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun successMoviesResponseTest(): Unit = runBlocking {
        val movie: MovieCardInfo = MovieCardInfo(
            ageRating = 18,
            alternativeName = "MovieNameAlt",
            countries = listOf(
                Country("Russia"),
                Country("Other Russia"),
                Country("Mother Russia")
            ),
            enName = null,
            genres = listOf(
                Genre("Жиза")
            ),
            id = 223,
            name = "Название фильма",
            poster = Poster(
                previewUrl = "previewUrl",
                url = "url"
            ),
            rating = Rating(
                kp = 9.9
            ),
            year = 1999
        )

        val moviePagingSource: MoviePagingSource =
            MoviePagingSource(kinopoiskRepository)

        whenever(kinopoiskRepository.getMovies(any(), any())).thenReturn(
            Response.success(
                200,
                PageResponse(listOf(movie), 20, 1, 34, 1000)
            )
        )

        val result = moviePagingSource.load(
            PagingSource.LoadParams.Append(
                1,
                20,
                false
            )
        )

        assertInstanceOf(PagingSource.LoadResult.Page::class.java, result)

        val pageResult = result as PagingSource.LoadResult.Page<Int, MovieCardInfo>

        assertIterableEquals(listOf(movie), pageResult.data)
        assertEquals(2, pageResult.nextKey)
    }
}