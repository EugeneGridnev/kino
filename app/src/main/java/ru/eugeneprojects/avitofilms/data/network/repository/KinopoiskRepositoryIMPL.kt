package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.api.KinopoiskAPI
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.filters.MovieSortType
import ru.eugeneprojects.avitofilms.data.models.filters.MovieTypeFilter
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieCardInfo
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieInfo
import javax.inject.Inject

class KinopoiskRepositoryIMPL @Inject constructor(private val api: KinopoiskAPI) :
    KinopoiskRepository {
    override suspend fun getMovies(
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<MovieCardInfo>> =
        api.getMovies(pageNumber, pageSize)

    override suspend fun getSearchedMovies(
        searchText: String,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<MovieCardInfo>> =
        api.getSearchedMovies(pageNumber, pageSize, searchText)

    override suspend fun getMovie(id: Int): Response<MovieInfo> = api.getMovie(id)
    override suspend fun getComments(
        movieId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Comment>> = api.getReviews(movieId, pageNumber, pageSize)

    override suspend fun getActors(
        movieId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<Actor>> = api.getActors(movieId, pageNumber, pageSize)

    override suspend fun getFilteredMovies(
        filters: MovieFilters,
        pageNumber: Int,
        pageSize: Int
    ): Response<PageResponse<MovieCardInfo>> {

        val queryMap: MutableMap<String, String> = mutableMapOf()

        when (filters.type) {
            MovieTypeFilter.ALL -> {}
            MovieTypeFilter.MOVIES -> queryMap[QUERY_KEY_MOVIE_TYPE] = QUERY_VALUE_MOVIES
            MovieTypeFilter.SERIES -> queryMap[QUERY_KEY_MOVIE_TYPE] = QUERY_VALUE_SERIES
        }

        when (filters.sort) {
            MovieSortType.YEAR -> queryMap[QUERY_KEY_SORT_BY] = QUERY_VALUE_YEAR
            MovieSortType.COUNTRY -> queryMap[QUERY_KEY_SORT_BY] = QUERY_VALUE_COUNTRIES_NAME
            MovieSortType.AGE_RATING -> queryMap[QUERY_KEY_SORT_BY] = QUERY_VALUE_AGE_RATING
        }
        queryMap[QUERY_KEY_SORT_DIRECTION] = QUERY_KEY_SORT_DIRECTION_VALUE

        queryMap[QUERY_KEY_RATING_KP] = filters.rating.let { "${it.first}-${it.last}" }

        return api.getFilteredMovies(pageNumber, pageSize, queryMap)
    }

    companion object {
        private const val QUERY_KEY_MOVIE_TYPE = "type"
        private const val QUERY_KEY_SORT_BY = "sortField"
        private const val QUERY_KEY_SORT_DIRECTION = "sortType"
        private const val QUERY_KEY_RATING_KP = "rating.kp"

        private const val QUERY_KEY_SORT_DIRECTION_VALUE = "-1"

        private const val QUERY_VALUE_MOVIES = "movie"
        private const val QUERY_VALUE_SERIES = "tv-series"
        private const val QUERY_VALUE_YEAR = "year"
        private const val QUERY_VALUE_COUNTRIES_NAME = "countries.name"
        private const val QUERY_VALUE_AGE_RATING = "ageRating"
    }
}