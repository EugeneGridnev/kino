package ru.eugeneprojects.avitofilms.data.models.comment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val author: String,
    val createdAt: String,
    val date: String,
    val id: Int,
    val movieId: Int,
    val review: String,
    val title: String,
    val type: String,
): Parcelable