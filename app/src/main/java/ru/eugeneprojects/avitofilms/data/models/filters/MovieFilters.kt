package ru.eugeneprojects.avitofilms.data.models.filters

data class MovieFilters(
    val type: MovieTypeFilter,
    val sort: MovieSortType,
    val rating: IntRange
)