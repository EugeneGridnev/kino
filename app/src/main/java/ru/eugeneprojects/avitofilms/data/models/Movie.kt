package ru.eugeneprojects.avitofilms.data.models

data class Movie(
    val ageRating: Int,
    val alternativeName: String?,
    val budget: Budget,
    val countries: List<Country>,
    val description: String,
    val enName: String?,
    val genres: List<Genre>,
    val id: Int,
    val movieLength: Int,
    val name: String?,
    val poster: Poster,
    val rating: Rating,
    val slogan: String,
    val year: Int
)