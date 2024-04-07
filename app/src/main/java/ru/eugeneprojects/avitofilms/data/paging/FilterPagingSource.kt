package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository

class FilterPagingSource (
    private val moviesRepository: MoviesRepository,
    private val filters: MovieFilters
) : BasePagingSource<Movie>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Movie>> {
        return moviesRepository.getFilteredMovies(filters, page, size)
    }
}