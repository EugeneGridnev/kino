package ru.eugeneprojects.avitofilms.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.eugeneprojects.avitofilms.BuildConfig
import ru.eugeneprojects.avitofilms.data.models.MoviesResponse

interface MoviesAPI {

    @GET("/v1.4/movie/search")
    suspend fun getMovies(
        @Query("query")
        searchText: String = "",
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<MoviesResponse>
}