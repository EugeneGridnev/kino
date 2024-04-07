package ru.eugeneprojects.avitofilms.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.eugeneprojects.avitofilms.BuildConfig
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.models.PageResponse

interface MoviesAPI {

    @GET("/v1.4/movie/search")
    suspend fun getMovies(
        @Query("page")
        pageNumber: Int,
        @Query("limit")
        pageSize: Int,
        @Query("query")
        searchText: String = "",
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<PageResponse<Movie>>

    @GET("/v1.4/movie")
    suspend fun getFilteredMovies(
        @Query("page")
        pageNumber: Int,
        @Query("limit")
        pageSize: Int,
        @QueryMap
        filters: Map<String, String>,
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<PageResponse<Movie>>
}