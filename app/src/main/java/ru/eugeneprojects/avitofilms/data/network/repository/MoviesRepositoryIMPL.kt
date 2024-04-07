package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.api.MoviesAPI
import ru.eugeneprojects.avitofilms.data.models.MoviesResponse
import javax.inject.Inject

class MoviesRepositoryIMPL @Inject constructor(private val api: MoviesAPI) : MoviesRepository {

    override suspend fun getMovies(
        searchText: String,
        pageNumber: Int,
        pageSize: Int
    ): Response<MoviesResponse> = api.getMovies(pageNumber, pageSize, searchText)
}