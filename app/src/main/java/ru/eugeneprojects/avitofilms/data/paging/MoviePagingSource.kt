package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieCardInfo
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository

class MoviePagingSource (
    private val kinopoiskRepository: KinopoiskRepository,
) : BasePagingSource<MovieCardInfo>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<MovieCardInfo>> {
        return kinopoiskRepository.getMovies( page, size)
    }
}