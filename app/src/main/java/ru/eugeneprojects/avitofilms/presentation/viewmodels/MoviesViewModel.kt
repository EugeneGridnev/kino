package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepository
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository
import ru.eugeneprojects.avitofilms.data.paging.MoviesPagingSource
import ru.eugeneprojects.avitofilms.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private var searchQuery: MutableLiveData<String> = MutableLiveData("")

    val isOnline = connectivityRepository.isConnected.asLiveData()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val movies = searchQuery
        .asFlow()
        .debounce(1000)
        .flatMapLatest {
            Pager(
                config = Constants.PAGING_CONFIG,
                pagingSourceFactory = { MoviesPagingSource(moviesRepository, it) }
            ).flow
        }.flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {

        if (searchQuery.value != query) {
            searchQuery.value = query
        }
    }


}