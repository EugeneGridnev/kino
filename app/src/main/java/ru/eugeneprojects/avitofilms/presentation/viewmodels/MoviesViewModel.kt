package ru.eugeneprojects.avitofilms.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    val isOnline = connectivityRepository.isConnected.asLiveData()

    fun getMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = Constants.PAGING_CONFIG,
            pagingSourceFactory = { MoviesPagingSource(moviesRepository, "") }
        ).flow.cachedIn(viewModelScope)


}