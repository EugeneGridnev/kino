package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters

interface MoviesRepository {

    suspend fun getMovies(searchText: String, pageNumber: Int, pageSize: Int): Response<PageResponse<Movie>>

    suspend fun getFilteredMovies(filters: MovieFilters, pageNumber: Int, pageSize: Int): Response<PageResponse<Movie>>
}