package ru.eugeneprojects.avitofilms.data.models.moviedescription

data class MovieCardInfo(
    val ageRating: Int,
    val alternativeName: String?,
    val countries: List<Country>,
    val enName: String?,
    val genres: List<Genre>,
    val id: Int,
    val name: String?,
    val poster: Poster?,
    val rating: Rating?,
    val year: Int?
)