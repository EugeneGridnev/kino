package ru.eugeneprojects.avitofilms.data.network.repository

import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.MoviesResponse

interface MoviesRepository {

    suspend fun getMovies(searchText: String): Response<MoviesResponse>
}