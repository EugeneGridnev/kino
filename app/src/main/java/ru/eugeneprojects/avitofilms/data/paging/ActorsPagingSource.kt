package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository

class ActorsPagingSource (
    private val moviesRepository: MoviesRepository,
    private val movieId: Int
    ) : BasePagingSource<Actor>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Actor>> {
        return moviesRepository.getActors(movieId, page, size)
    }
}