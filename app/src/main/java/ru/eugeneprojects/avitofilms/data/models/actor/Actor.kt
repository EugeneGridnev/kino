package ru.eugeneprojects.avitofilms.data.models.actor

data class Actor(
    val enName: String,
    val id: Int,
    val name: String,
    val photo: String,
    val profession: List<Profession>,
)