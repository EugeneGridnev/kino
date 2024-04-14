package ru.eugeneprojects.avitofilms.utils

fun String?.filterBlank() = takeUnless { it.isNullOrBlank() }