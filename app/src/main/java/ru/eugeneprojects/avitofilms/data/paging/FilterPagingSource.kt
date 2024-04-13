package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieCardInfo
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository

class FilterPagingSource (
    private val kinopoiskRepository: KinopoiskRepository,
    private val filters: MovieFilters
) : BasePagingSource<MovieCardInfo>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<MovieCardInfo>> {
        return kinopoiskRepository.getFilteredMovies(filters, page, size)
    }
}