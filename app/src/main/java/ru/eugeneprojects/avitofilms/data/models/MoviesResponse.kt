package ru.eugeneprojects.avitofilms.data.models

data class MoviesResponse(
    val movies: List<Movie>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)