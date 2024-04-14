package ru.eugeneprojects.avitofilms

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import ru.eugeneprojects.avitofilms.di.ServiceModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import java.time.OffsetDateTime
import java.time.ZoneOffset


class ApiTests() {

    private fun setUpRetrofit(url: String) =
        ServiceModule.provideMoviesApi(ServiceModule.provideGson(), ServiceModule.ApiUrl(url))

    @Test
    fun getMoviesTest(): Unit = runBlocking {
        val server = MockWebServer()
        server.enqueue(MockResponse().apply {
            setBody("""
            {
                "docs": [
                    {
                    "rating": {
                        "kp": 8.823,
                        "imdb": 8.5
                    },
                    "movieLength": 112,
                    "id": 535341,
                    "name": "1+1",
                    "description": "Пострадав в результате несчастного случая, богатый аристократ Филипп нанимает в помощники человека, который менее всего подходит для этой работы, – молодого жителя предместья Дрисса, только что освободившегося из тюрьмы. Несмотря на то, что Филипп прикован к инвалидному креслу, Дриссу удается привнести в размеренную жизнь аристократа дух приключений.",
                    "slogan": "Sometimes you have to reach into someone else's world to find out what's missing in your own.",
                    "year": 2011,
                    "budget": {
                        "value": 9500000,
                        "currency": "€"
                    },
                    "poster": {
                        "url": "https://image.openmoviedb.com/kinopoisk-images/1946459/bf93b465-1189-4155-9dd1-cb9fb5cb1bb5/orig",
                        "previewUrl": "https://image.openmoviedb.com/kinopoisk-images/1946459/bf93b465-1189-4155-9dd1-cb9fb5cb1bb5/x1000"
                    },
                    "genres": [
                    {
                        "name": "драма"
                    },
                    {
                        "name": "комедия"
                    },
                    {
                        "name": "биография"
                    }
                    ],
                    "countries": [
                    {
                        "name": "Франция"
                    }
                    ],
                    "alternativeName": "Intouchables",
                    "enName": null,
                    "ageRating": 18
                }
                ],
                "total": 1056227,
                "limit": 1,
                "page": 1,
                "pages": 1056227
            }"""
            )
        })

        server.start()

        val serverUrl = server.url("")
        val movieApi = setUpRetrofit(serverUrl.toString())

        val response = movieApi.getMovies(1, 1, "test-api-key")
        assertNotNull(response.body())
        val body = response.body()!!
        assertEquals(200, response.code())
        assertEquals(1, body.page)

        assertEquals(1, body.docs.size)

        with(body.docs[0]) {
            assertAll(
                { assertEquals(535341, id) },
                { assertNull( enName) },
                { assertEquals("1+1", name) },
                { assertEquals(18, ageRating) }
            )
        }

        val request = server.takeRequest()

        assertEquals("test-api-key", request.getHeader("X-API-KEY"))

        with(request.requestUrl!!) {
            assertAll(
                { assertEquals("/v1.4/movie", toUrl().path) },
                { assertEquals("1", queryParameter("page")) },
                { assertEquals("1", queryParameter("limit")) }
            )
        }

        server.shutdown()
    }

    @Test
    fun getSearchedMoviesTest(): Unit = runBlocking {
        val server = MockWebServer()
        server.enqueue(MockResponse().apply {
            setBody(
                """{
                    "docs": [
                    {
                        "id": 535341,
                        "name": "1+1",
                        "alternativeName": "Intouchables",
                        "enName": "",                     
                        "year": 2011,
                        "description": "Пострадав в результате несчастного случая, богатый аристократ Филипп нанимает в помощники человека, который менее всего подходит для этой работы, – молодого жителя предместья Дрисса, только что освободившегося из тюрьмы. Несмотря на то, что Филипп прикован к инвалидному креслу, Дриссу удается привнести в размеренную жизнь аристократа дух приключений.",
                        "movieLength": 112,
                        "ageRating": 18,
                        "poster": {
                        "url": "https://image.openmoviedb.com/kinopoisk-images/1946459/bf93b465-1189-4155-9dd1-cb9fb5cb1bb5/orig",
                        "previewUrl": "https://image.openmoviedb.com/kinopoisk-images/1946459/bf93b465-1189-4155-9dd1-cb9fb5cb1bb5/x1000"
                        },
                        "rating": {
                            "kp": 8.823,
                            "imdb": 8.5
                        },
                        "genres": [
                            {
                                "name": "драма"
                            },
                            {
                                "name": "комедия"
                            },
                            {
                                "name": "биография"
                            }
                            ],
                            "countries": [
                            {
                                "name": "Франция"
                            }
                        ]
                    }
                    ],
                    "total": 29,
                    "limit": 1,
                    "page": 1,
                    "pages": 29
                }"""
            )
        })

        server.start()

        val serverUrl = server.url("")
        val movieApi = setUpRetrofit(serverUrl.toString())

        val response = movieApi.getSearchedMovies(1, 1, "1+1", "test-api-key")
        assertNotNull(response.body())
        val body = response.body()!!
        assertEquals(200, response.code())
        assertEquals(1, body.page)

        val request = server.takeRequest()

        assertEquals(1, body.docs.size)

        with(body.docs[0]) {
            assertAll(
                { assertEquals(535341, id) },
                { assertEquals("", enName) },
                { assertEquals("1+1", name) },
                { assertEquals(18, ageRating) },
            )
        }

        assertEquals("test-api-key", request.getHeader("X-API-KEY"))

        with(request.requestUrl!!) {
            assertAll(
                { assertEquals("/v1.4/movie/search", toUrl().path) },
                { assertEquals("1", queryParameter("page")) },
                { assertEquals("1", queryParameter("limit")) },
                { assertEquals("1+1", queryParameter("query")) }
            )
        }

        server.shutdown()
    }

    @Test
    fun getActorsTest(): Unit = runBlocking {
        val server = MockWebServer()
        server.enqueue(MockResponse().apply {
            setBody(
                """{
                    "docs": [
                    {
                        "id": 3441044,
                        "enName": "Da Han",
                        "name": "Да Хань",
                        "photo": null
                    }
                    ],
                    "total": 52,
                    "limit": 1,
                    "page": 1,
                    "pages": 52
                }"""
            )
        })

        server.start()

        val serverUrl = server.url("")
        val movieApi = setUpRetrofit(serverUrl.toString())

        val response = movieApi.getActors(666, 1, 1, apiKey = "test-api-key")
        assertNotNull(response.body())
        val body = response.body()!!
        assertEquals(200, response.code())
        assertEquals(1, body.page)

        assertEquals(1, body.docs.size)

        with(body.docs[0]) {
            assertAll(
                { assertEquals(3441044, id) },
                { assertEquals("Da Han", enName) },
                { assertEquals("Да Хань", name) },
                { assertNull(photo) }
            )
        }

        val request = server.takeRequest()

        assertEquals("test-api-key", request.getHeader("X-API-KEY"))

        with(request.requestUrl!!) {
            assertAll(
                { assertEquals("/v1.4/person", toUrl().path) },
                { assertEquals("1", queryParameter("page")) },
                { assertEquals("1", queryParameter("limit")) },
                { assertNull(queryParameter("selectFields")) }
            )
        }

        server.shutdown()
    }

    @Test
    fun getCommentsTest(): Unit = runBlocking {
        val server = MockWebServer()
        server.enqueue(MockResponse().apply {
            setBody(
                """{
                    "docs": [
                    {
                        "id": 3371711,
                        "movieId": 1394131,
                        "title": "На первом и последнем плане...",
                        "type": "Негативный",
                        "date": "2024-04-13T11:14:41.000Z",
                        "author": "richterds - 6556"
                    }
                    ],
                    "total": 809468,
                    "limit": 1,
                    "page": 1,
                    "pages": 809468
                }"""
            )
        })

        server.start()

        val serverUrl = server.url("")
        val movieApi = setUpRetrofit(serverUrl.toString())

        val response = movieApi.getReviews(666, 1, 1, "test-api-key")
        assertNotNull(response.body())
        val body = response.body()!!
        assertEquals(200, response.code())
        assertEquals(1, body.page)

        assertEquals(1, body.docs.size)

        with(body.docs[0]) {
            assertAll(
                { assertEquals(3371711, id) },
                { assertEquals("На первом и последнем плане...", title) },
                { assertEquals("Негативный", type) },
                { assertEquals(OffsetDateTime.of(2024,4,13,11,14,41,0, ZoneOffset.UTC), date) },
                { assertEquals("richterds - 6556", author) }
            )
        }

        val request = server.takeRequest()

        assertEquals("test-api-key", request.getHeader("X-API-KEY"))

        with(request.requestUrl!!) {
            assertAll(
                { assertEquals("/v1.4/review", toUrl().path) },
                { assertEquals("1", queryParameter("page")) },
                { assertEquals("1", queryParameter("limit")) },
                { assertEquals("666", queryParameter("movieId")) },
                { assertEquals(null, queryParameter("selectFields")) }
            )
        }

        server.shutdown()
    }

    @Test
    fun getMovieTest(): Unit = runBlocking {
        val server = MockWebServer()
        server.enqueue(MockResponse().apply {
            setBody(
                """
                    {
                        "id": 535341,
                        "name": "1+1",
                        "alternativeName": "Intouchables",
                        "enName": "",
                        "type": "movie",
                        "year": 2011,
                        "description": "Пострадав в результате несчастного случая, богатый аристократ Филипп нанимает в помощники человека, который менее всего подходит для этой работы, – молодого жителя предместья Дрисса, только что освободившегося из тюрьмы. Несмотря на то, что Филипп прикован к инвалидному креслу, Дриссу удается привнести в размеренную жизнь аристократа дух приключений.",
                        "shortDescription": "Аристократ на коляске нанимает в сиделки бывшего заключенного. Искрометная французская комедия с Омаром Си",
                        "movieLength": 112,
                        "ticketsOnSale": false,
                        "totalSeriesLength": null,
                        "seriesLength": null,
                        "ageRating": 18,
                        "poster": {
                        "url": "https://image.openmoviedb.com/kinopoisk-images/1946459/bf93b465-1189-4155-9dd1-cb9fb5cb1bb5/orig",
                        "previewUrl": "https://image.openmoviedb.com/kinopoisk-images/1946459/bf93b465-1189-4155-9dd1-cb9fb5cb1bb5/x1000"
                    },
                        "rating": {
                        "kp": 8.823,
                        "imdb": 8.5
                    },
                        "votes": {
                        "kp": 2006683,
                        "imdb": 923505,
                        "filmCritics": 129,
                        "russianFilmCritics": 12,
                        "await": 15
                    },
                        "genres": [
                        {
                            "name": "драма"
                        },
                        {
                            "name": "комедия"
                        },
                        {
                            "name": "биография"
                        }
                        ],
                        "countries": [
                        {
                            "name": "Франция"
                        }
                        ]
                    }"""
            )
        })

        server.start()

        val serverUrl = server.url("")
        val movieApi = setUpRetrofit(serverUrl.toString())

        val response = movieApi.getMovie(535341,"test-api-key")
        assertNotNull(response.body())
        val body = response.body()!!
        assertEquals(200, response.code())

        with(body) {
            assertAll(
                { assertEquals(535341, id) },
                { assertEquals("1+1", name) },
                { assertEquals(2011, year) }
            )
        }

        val request = server.takeRequest()

        assertEquals("test-api-key", request.getHeader("X-API-KEY"))

        with(request.requestUrl!!) {
            assertAll(
                { assertEquals("/v1.4/movie/535341", toUrl().path) }
            )
        }

        server.shutdown()
    }

    @Test
    fun getFilteredMoviesTest(): Unit = runBlocking {
        val server = MockWebServer()
        server.enqueue(MockResponse().apply {
            setBody(
                """{
                    "docs": [
                    {
                        "id": 5458837,
                        "name": "96-я церемония вручения премии «Оскар»",
                        "alternativeName": "96th Academy Awards",
                        "enName": null,
                        "names": [
                        {
                            "name": "96-я церемония вручения премии «Оскар»",
                            "language": "RU",
                            "type": "Russian title on kinopoisk"
                        },
                        {
                            "name": "96th Academy Awards",
                            "language": null,
                            "type": "Original title on kinopoisk"
                        }
                        ],
                        "type": "movie",
                        "typeNumber": 1,
                        "year": 2024,
                        "description": null,
                        "status": null,
                        "rating": {
                        "kp": 7.579,
                        "imdb": 6.8,
                        "filmCritics": 0,
                        "russianFilmCritics": 0,
                        "await": null
                    },
                        "votes": {
                        "kp": 149,
                        "imdb": 1338,
                        "filmCritics": 0,
                        "russianFilmCritics": 0,
                        "await": 0
                    },
                        "movieLength": 157,
                        "totalSeriesLength": null,
                        "seriesLength": null,
                        "ratingMpaa": null,
                        "ageRating": null,
                        "poster": {
                        "url": "https://image.openmoviedb.com/kinopoisk-images/10592371/c376a157-0bec-470d-b902-7c0f08bfcc31/orig",
                        "previewUrl": "https://image.openmoviedb.com/kinopoisk-images/10592371/c376a157-0bec-470d-b902-7c0f08bfcc31/x1000"
                    },
                        "backdrop": {
                        "url": null,
                        "previewUrl": null
                    },
                        "genres": [
                        {
                            "name": "церемония"
                        }
                        ],
                        "countries": [
                        {
                            "name": "США"
                        }
                        ],
                        "top10": null,
                        "top250": null,
                        "isSeries": false,
                        "ticketsOnSale": false
                    }
                    ],
                    "total": 42373,
                    "limit": 1,
                    "page": 1,
                    "pages": 42373
                }"""
            )
        })

        server.start()

        val serverUrl = server.url("")
        val movieApi = setUpRetrofit(serverUrl.toString())

        val queryMap: MutableMap<String, String> = mutableMapOf()
        queryMap["type"] = "movie"
        queryMap["sortField"] = "year"
        queryMap["sortType"] = "-1"
        queryMap["rating.kp"] = "6-10"

        val response = movieApi.getFilteredMovies(
            1,
            1,
            queryMap,
            "test-api-key"
        )
        assertNotNull(response.body())
        val body = response.body()!!
        assertEquals(200, response.code())
        assertEquals(1, body.page)

        assertEquals(1, body.docs.size)

        with(body.docs[0]) {
            assertAll(
                { assertEquals(5458837, id) },
                { assertEquals("96-я церемония вручения премии «Оскар»", name) },
                { assertEquals(2024, year) }
            )
        }

        val request = server.takeRequest()

        assertEquals("test-api-key", request.getHeader("X-API-KEY"))

        with(request.requestUrl!!) {
            assertAll(
                { assertEquals("/v1.4/movie", toUrl().path) },
                { assertEquals("1", queryParameter("page")) },
                { assertEquals("1", queryParameter("limit")) },
                { assertEquals("year", queryParameter("sortField")) },
                { assertEquals("-1", queryParameter("sortType")) },
                { assertEquals("movie", queryParameter("type")) },
                { assertEquals("6-10", queryParameter("rating.kp")) }
            )
        }

        server.shutdown()
    }
}