package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieCardInfo
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieInfo


interface KinopoiskRepository {

    suspend fun getMovies(pageNumber: Int, pageSize: Int): Response<PageResponse<MovieCardInfo>>

    suspend fun getSearchedMovies(searchText: String, pageNumber: Int, pageSize: Int): Response<PageResponse<MovieCardInfo>>

    suspend fun getFilteredMovies(filters: MovieFilters, pageNumber: Int, pageSize: Int): Response<PageResponse<MovieCardInfo>>

    suspend fun getMovie(id: Int): Response<MovieInfo>

    suspend fun getComments(movieId: Int, pageNumber: Int, pageSize: Int): Response<PageResponse<Comment>>

    suspend fun getActors(movieId: Int, pageNumber: Int, pageSize: Int): Response<PageResponse<Actor>>

}