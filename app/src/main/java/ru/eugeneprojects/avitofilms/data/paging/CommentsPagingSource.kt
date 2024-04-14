package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.PageResponse
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository

class CommentsPagingSource(
    private val kinopoiskRepository: KinopoiskRepository,
    private val movieId: Int
) : BasePagingSource<Comment>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Comment>> {
        return kinopoiskRepository.getComments(movieId, page, size)
    }
}