package ru.eugeneprojects.avitofilms.data.models.comment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.OffsetDateTime

@Parcelize
data class Comment(
    val author: String?,
    val date: OffsetDateTime?,
    val id: Int?,
    val movieId: Int?,
    val review: String?,
    val title: String?,
    val type: String?,
) : Parcelable {

    companion object {
        const val TYPE_POSITIVE = "Позитивный"
        const val TYPE_NEGATIVE = "Негативный"
        const val TYPE_NEUTRAL = "Нейтральный"
    }
}