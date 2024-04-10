package ru.eugeneprojects.avitofilms.data.paging

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.PageResponse
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository

class ActorsPagingSource (
    private val kinopoiskRepository: KinopoiskRepository,
    private val movieId: Int
    ) : BasePagingSource<Actor>() {
    override suspend fun requestPage(page: Int, size: Int): Response<PageResponse<Actor>> {
        return kinopoiskRepository.getActors(movieId, page, size)
    }
}