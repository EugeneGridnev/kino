package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.Movie
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.PageResponse
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository

class MoviesPagingSource (
    private val kinopoiskRepository: KinopoiskRepository,
    private val query: String
) : BasePagingSource<Movie>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Movie>> {
        return kinopoiskRepository.getMovies(query, page, size)
    }
}