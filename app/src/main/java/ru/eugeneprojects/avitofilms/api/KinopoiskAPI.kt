package ru.eugeneprojects.avitofilms.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.eugeneprojects.avitofilms.BuildConfig
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.Movie
import ru.eugeneprojects.avitofilms.data.models.movieDescription.MovieInfo

interface KinopoiskAPI {

    @GET("/v1.4/movie")
    suspend fun getMovies(
        @Query("page")
        pageNumber: Int,
        @Query("limit")
        pageSize: Int,
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<PageResponse<Movie>>

    @GET("/v1.4/movie/search")
    suspend fun getSearchedMovies(
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

    @GET("/v1.4/movie/{id}")
    suspend fun getMovie(
        @Path("id")
        id: Int,
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<MovieInfo>

    @GET("/v1.4/person")
    suspend fun getActors(
        @Query("movies.id")
        movieId: Int,
        @Query("page")
        pageNumber: Int,
        @Query("limit")
        pageSize: Int,
        @Query("profession.value")
        profession: String = "Актер",
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<PageResponse<Actor>>

    @GET("/v1.4/review")
    suspend fun getReviews(
        @Query("movieId")
        movieId: Int,
        @Query("page")
        pageNumber: Int,
        @Query("limit")
        pageSize: Int,
        @Header("X-API-KEY")
        apiKey: String = BuildConfig.API_KEY
    ): Response<PageResponse<Comment>>

}