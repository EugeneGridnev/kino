package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.Movie
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.movieDescription.MovieInfo
import ru.eugeneprojects.avitofilms.data.models.poster.Poster


interface MoviesRepository {

    suspend fun getMovies(searchText: String, pageNumber: Int, pageSize: Int): Response<PageResponse<Movie>>

    suspend fun getFilteredMovies(filters: MovieFilters, pageNumber: Int, pageSize: Int): Response<PageResponse<Movie>>

    suspend fun getMovie(id: Int): Response<MovieInfo>

    suspend fun getComments(movieId: Int, pageNumber: Int, pageSize: Int): Response<PageResponse<Comment>>

    suspend fun getPosters(movieId: Int,pageNumber: Int, pageSize: Int): Response<PageResponse<Poster>>

    suspend fun getActors(movieId: Int, pageNumber: Int, pageSize: Int): Response<PageResponse<Actor>>

}