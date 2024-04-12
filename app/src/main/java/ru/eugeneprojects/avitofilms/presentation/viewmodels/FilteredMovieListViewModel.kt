package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.LiveData
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
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepository
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository
import ru.eugeneprojects.avitofilms.data.paging.FilterPagingSource
import ru.eugeneprojects.avitofilms.utils.Constants
import ru.eugeneprojects.avitofilms.utils.Constants.DEFAULT_FILTERS
import javax.inject.Inject

@HiltViewModel
class FilteredMovieListViewModel @Inject constructor(
    private val kinopoiskRepository: KinopoiskRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _filters = MutableLiveData(DEFAULT_FILTERS)
    var filters: LiveData<MovieFilters?> = _filters

    val isOnline = connectivityRepository.isConnected.asLiveData()


    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = _filters.asFlow()
        .distinctUntilChanged()
        .flatMapLatest {movieFilters ->
            Pager(
                config = Constants.PAGING_CONFIG,
                pagingSourceFactory = { FilterPagingSource(kinopoiskRepository, movieFilters!!) }
            ).flow
        }.flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    fun setFilter(filters: MovieFilters) {

        _filters.value = filters
    }

}