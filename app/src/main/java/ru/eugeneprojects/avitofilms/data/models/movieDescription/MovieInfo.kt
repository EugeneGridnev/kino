package ru.eugeneprojects.avitofilms.data.models.movieDescription

data class MovieInfo(
    val ageRating: Int,
    val alternativeName: String,
    val budget: Budget,
    val countries: List<Country>,
    val description: String,
    val enName: String,
    val externalId: ExternalId,
    val genres: List<Genre>,
    val id: Int,
    val isSeries: Boolean,
    val movieLength: Int,
    val name: String,
    val poster: Poster,
    val rating: Rating,
    val shortDescription: String,
    val slogan: String,
    val typeNumber: Int,
    val updatedAt: String,
    val year: Int
)