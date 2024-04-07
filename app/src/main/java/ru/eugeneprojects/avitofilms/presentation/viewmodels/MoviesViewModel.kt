package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepository
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository
import ru.eugeneprojects.avitofilms.data.paging.FilterPagingSource
import ru.eugeneprojects.avitofilms.data.paging.MoviesPagingSource
import ru.eugeneprojects.avitofilms.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val state = MutableLiveData<State>(State.Search(""))

    val isOnline = connectivityRepository.isConnected.asLiveData()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val movies = state.asFlow()
        .distinctUntilChanged { old, new ->
            old == new
        }.debounce(1000)
        .flatMapLatest {state ->
            val pagingSourceFactory: () -> PagingSource<Int, Movie> = when (state) {
                is State.Filter -> { { FilterPagingSource(moviesRepository, state.movieFilters) } }
                is State.Search -> { { MoviesPagingSource(moviesRepository, state.query) } }
            }
                Pager(
                    config = Constants.PAGING_CONFIG,
                    pagingSourceFactory = pagingSourceFactory
                )
            .flow
        }.flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {

        state.value = State.Search(query)
    }

    fun setFilter(filters: MovieFilters) {

        state.value = State.Filter(filters)
    }

    sealed class State {

        data class Filter(
            val movieFilters: MovieFilters
        ): State()
        data class Search(
            val query: String
        ): State()
    }
}