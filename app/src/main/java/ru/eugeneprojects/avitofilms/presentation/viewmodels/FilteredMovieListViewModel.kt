package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.filters.MovieSortType
import ru.eugeneprojects.avitofilms.data.models.filters.MovieTypeFilter
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepository
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository
import ru.eugeneprojects.avitofilms.data.paging.FilterPagingSource
import ru.eugeneprojects.avitofilms.utils.Constants
import ru.eugeneprojects.avitofilms.utils.Constants.DEFAULT_FILTERS
import javax.inject.Inject

@HiltViewModel
class FilteredMovieListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val state: MutableLiveData<MovieFilters?> = MutableLiveData<MovieFilters?>(DEFAULT_FILTERS)

    val isOnline = connectivityRepository.isConnected.asLiveData()


    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = state.asFlow()
        .distinctUntilChanged()
        .flatMapLatest {movieFilters ->
            Pager(
                config = Constants.PAGING_CONFIG,
                pagingSourceFactory = { FilterPagingSource(moviesRepository, movieFilters!!) }
            ).flow
        }.flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    fun setFilter(filters: MovieFilters) {

        state.value = filters
    }

}