package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.PageResponse
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository

class CommentsPagingSource (
    private val moviesRepository: MoviesRepository,
    private val movieId: Int
) : BasePagingSource<Comment>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Comment>> {
        return moviesRepository.getComments(movieId, page, size)
    }
}