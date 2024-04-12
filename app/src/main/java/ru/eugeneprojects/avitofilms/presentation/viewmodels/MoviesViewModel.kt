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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepository
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository
import ru.eugeneprojects.avitofilms.data.paging.SearchMoviesPagingSource
import ru.eugeneprojects.avitofilms.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val kinopoiskRepository: KinopoiskRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val state = MutableLiveData("")

    val isOnline = connectivityRepository.isConnected.asLiveData()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val movies = state.asFlow()
        .distinctUntilChanged()
        .debounce(1000)
        .flatMapLatest {query ->
            Pager(
                config = Constants.PAGING_CONFIG,
                pagingSourceFactory = { SearchMoviesPagingSource(kinopoiskRepository, query) }
            ).flow
        }.flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {

        state.value = query
    }
}