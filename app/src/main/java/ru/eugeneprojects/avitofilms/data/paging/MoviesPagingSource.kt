package ru.eugeneprojects.avitofilms.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository
import ru.eugeneprojects.avitofilms.utils.Constants

class MoviesPagingSource (
    private val moviesRepository: MoviesRepository,
    private val query: String
) : BasePagingSource<Movie>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Movie>> {
        return moviesRepository.getMovies(query, page, size)
    }
}