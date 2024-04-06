package ru.eugeneprojects.avitofilms.data.network.repository

import ru.eugeneprojects.avitofilms.api.MoviesAPI
import javax.inject.Inject

class MoviesRepositoryIMPL @Inject constructor(private val api: MoviesAPI) : MoviesRepository {

    override suspend fun getMovies(searchText: String) = api.getMovies(searchText)
}