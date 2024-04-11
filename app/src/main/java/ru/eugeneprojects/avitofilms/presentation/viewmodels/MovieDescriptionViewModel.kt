package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.http.HTTP
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.data.models.movieDescription.MovieInfo
import ru.eugeneprojects.avitofilms.data.models.movieDescription.Poster
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository
import ru.eugeneprojects.avitofilms.data.paging.ActorsPagingSource
import ru.eugeneprojects.avitofilms.data.paging.CommentsPagingSource
import ru.eugeneprojects.avitofilms.data.paging.MoviesPagingSource
import ru.eugeneprojects.avitofilms.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MovieDescriptionViewModel @Inject constructor(
    private val kinopoiskRepository: KinopoiskRepository
) : ViewModel() {

    private val _descriptionState: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val description: StateFlow<State> = _descriptionState


    val comments: Flow<PagingData<Comment>> = createPagingSource { CommentsPagingSource(kinopoiskRepository, it.id) }

    val actors: Flow<PagingData<Actor>> = createPagingSource { ActorsPagingSource(kinopoiskRepository, it.id) }

    fun getMovie(id: Int) {
        viewModelScope.launch {
            fetchMovie(id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T: Any>createPagingSource(factory: (MovieInfo) -> PagingSource<Int, T>) =
        _descriptionState.flatMapLatest { when (it) {
            State.Loading, is State.Error -> flow{ emit(PagingData.empty())}
            is State.Movie -> Pager(
                config = Constants.PAGING_CONFIG,
                pagingSourceFactory = { factory(it.data) }
            ).flow
                .flowOn(Dispatchers.IO)
                .cachedIn(viewModelScope)
        }
    }

    private suspend fun fetchMovie(id: Int) {

        _descriptionState.value =
        try {
            val response = kinopoiskRepository.getMovie(id)

             if (response.isSuccessful) {
                 val movie = checkNotNull(response.body())
                 State.Movie(movie)
            } else {
                State.Error(HttpException(response))
            }
        } catch (e: Exception) {
            State.Error(e)
        }
    }

    sealed class State() {
        data object Loading: State()

        data class Movie(val data: MovieInfo): State()

        data class Error(
            val error: Exception
        ): State()
    }

}