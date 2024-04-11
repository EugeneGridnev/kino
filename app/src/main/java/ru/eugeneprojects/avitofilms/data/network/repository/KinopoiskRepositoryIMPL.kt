package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.api.KinopoiskAPI
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.Movie
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.filters.MovieSortType
import ru.eugeneprojects.avitofilms.data.models.filters.MovieTypeFilter
import ru.eugeneprojects.avitofilms.data.models.movieDescription.MovieInfo
import ru.eugeneprojects.avitofilms.data.models.poster.Poster
import javax.inject.Inject

class KinopoiskRepositoryIMPL @Inject constructor(private val api: KinopoiskAPI) :
    KinopoiskRepository {

    override suspend fun getMovies(
        searchText: String,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Movie>> = api.getMovies(pageNumber, pageSize, searchText)

    override suspend fun getMovie(id: Int): Response<MovieInfo> = api.getMovie(id)
    override suspend fun getComments(
        movieId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Comment>> = api.getReviews(movieId, pageNumber, pageSize)

    override suspend fun getPosters(
        movieId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Poster>> = api.getPosters(movieId, pageNumber, pageSize)

    override suspend fun getActors(
        movieId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Actor>> = api.getActors(movieId, pageNumber, pageSize)

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