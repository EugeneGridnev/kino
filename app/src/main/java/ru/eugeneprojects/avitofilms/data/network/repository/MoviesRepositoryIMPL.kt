package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.api.MoviesAPI
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.filters.MovieSortType
import ru.eugeneprojects.avitofilms.data.models.filters.MovieTypeFilter
import javax.inject.Inject

class MoviesRepositoryIMPL @Inject constructor(private val api: MoviesAPI) : MoviesRepository {

    override suspend fun getMovies(
        searchText: String,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Movie>> = api.getMovies(pageNumber, pageSize, searchText)

    override suspend fun getFilteredMovies(
        filters: MovieFilters,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Movie>> {

        val queryMap: MutableMap<String, String> = mutableMapOf()

        when (filters.type) {
            MovieTypeFilter.ALL -> {}
            MovieTypeFilter.MOVIES -> queryMap["type"] = "movie"
            MovieTypeFilter.SERIES -> queryMap["type"] = "tv-series"
        }

        when (filters.sort) {
            MovieSortType.YEAR -> queryMap["sortField"] = "year"
            MovieSortType.COUNTRY ->  queryMap["sortField"] = "countries.name"
            MovieSortType.AGE_RATING ->  queryMap["sortField"] = "ageRating"
        }
        queryMap["sortType"] = "-1"

        queryMap["rating.kp"] = filters.rating.let { "${it.first}-${it.last}" }

        return api.getFilteredMovies(pageNumber, pageSize, queryMap)
    }
}