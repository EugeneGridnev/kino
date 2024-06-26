package ru.eugeneprojects.avitofilms.data.models

data class PageResponse<T>(
    val docs: List<T>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)