package ru.eugeneprojects.avitofilms

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import ru.eugeneprojects.avitofilms.di.ServiceModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull


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
                "fees": {
                "world": {
                "value": 426588510,
                "currency": "$"
            },
                "russia": {
                "value": 1725813,
                "currency": "$"
            },
                "usa": {
                "value": 10198820,
                "currency": "$"
            }
            },
                "rating": {
                "kp": 8.823,
                "imdb": 8.5,
                "filmCritics": 6.8,
                "russianFilmCritics": 100,
                "await": null
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
}