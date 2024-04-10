package ru.eugeneprojects.avitofilms.data.models.poster

data class Poster(
    val createdAt: String,
    val height: Int,
    val language: String,
    val movieId: Int,
    val previewUrl: String,
    val type: String,
    val updatedAt: String,
    val url: String,
    val width: Int
)