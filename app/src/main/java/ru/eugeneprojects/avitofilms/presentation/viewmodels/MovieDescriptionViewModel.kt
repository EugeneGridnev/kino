package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.eugeneprojects.avitofilms.data.models.movieDescription.MovieInfo
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository
import javax.inject.Inject

@HiltViewModel
class MovieDescriptionViewModel @Inject constructor(
    private val kinopoiskRepository: KinopoiskRepository
) : ViewModel() {

    private val _descriptionState: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val description: StateFlow<State> = _descriptionState

    fun getMovie(id: Int) {
        viewModelScope.launch {
            fetchMovie(id)
        }
    }

    private suspend fun fetchMovie(id: Int) {

        try {
            val response = kinopoiskRepository.getMovie(id)

             if (response.isSuccessful) {
                 val movie = checkNotNull(response.body())
                _descriptionState.value = State.Movie(movie)
            }
        } catch (e: Exception) {
            //return PagingSource.LoadResult.Error(e)
        }
    }

    sealed class State() {
        data object Loading: State()
        data class Movie(val data: MovieInfo): State()


    }

}