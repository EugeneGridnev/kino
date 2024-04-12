package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.eugeneprojects.avitofilms.util.simpleScan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.adapters.MoviesLoadStateAdapter
import ru.eugeneprojects.avitofilms.adapters.MoviesPagingAdapter
import ru.eugeneprojects.avitofilms.databinding.FragmentMoviesListBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MoviesViewModel

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var binding: FragmentMoviesListBinding? = null

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesPagingAdapter: MoviesPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMoviesList()
        observeMovies()
        setUpFabButton()
        handleSearchChanges()
        handleScrollingToTopWhenSearching(moviesPagingAdapter)

        viewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (isOnline) {
                moviesPagingAdapter.retry()
            } else {
                Toast.makeText(activity, resources.getString(R.string.network_error_message), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setUpMoviesList() {

        moviesPagingAdapter = MoviesPagingAdapter()

        binding?.recyclerViewMovies?.layoutManager = LinearLayoutManager(activity)
        binding?.recyclerViewMovies?.adapter = moviesPagingAdapter.withLoadStateFooter(MoviesLoadStateAdapter())

        setOnMovieClick()

        initSwipeToRefresh(moviesPagingAdapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                moviesPagingAdapter.loadStateFlow.collect { loadStates ->
                    binding?.swipeRefresh?.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
                }
            }
        }

        moviesPagingAdapter.addLoadStateListener { combinedLoadStates ->
            val refreshState = combinedLoadStates.refresh
            binding?.recyclerViewMovies?.isVisible = refreshState != LoadState.Loading
            binding?.progressBar?.isVisible = refreshState == LoadState.Loading

            if (refreshState is LoadState.Error) {
                Toast.makeText(activity,resources.getString(R.string.toast_load_error_message), Toast.LENGTH_SHORT).show()
            }

            if (combinedLoadStates.source.refresh is LoadState.NotLoading && combinedLoadStates.append.endOfPaginationReached && moviesPagingAdapter.itemCount == 0) {
                binding?.recyclerViewMovies?.isVisible = false
                binding?.textViewStub?.isVisible = true
            } else {
                binding?.recyclerViewMovies?.isVisible = true
                binding?.textViewStub?.isVisible = false
            }
        }
    }

    private fun observeMovies() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchedMovies.collectLatest(moviesPagingAdapter::submitData)
            }
        }
    }

    private fun setOnMovieClick() {

        moviesPagingAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", it.id)
            }
            findNavController().navigate(
                R.id.action_moviesListFragment_to_movieDescriptionFragment,
                bundle
            )
        }
    }

    private fun initSwipeToRefresh(adapter: MoviesPagingAdapter) {

        binding?.swipeRefresh?.setOnRefreshListener { adapter.refresh() }
    }

    private fun handleSearchChanges() {

        binding?.searchEditText?.doOnTextChanged { searchQuery, _, _, _ ->
            viewModel.setSearchQuery(searchQuery.toString())
        }
    }

    private fun handleScrollingToTopWhenSearching(adapter: MoviesPagingAdapter) = lifecycleScope.launch {
        // list should be scrolled to the 1st item (index = 0) if data has been reloaded:
        // (prev state = Loading, current state = NotLoading)
        getRefreshLoadStateFlow(adapter)
            .simpleScan(2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    binding?.recyclerViewMovies?.scrollToPosition(0)
                }
            }
    }

    private fun getRefreshLoadStateFlow(adapter: MoviesPagingAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private fun setUpFabButton() {
        binding?.fabAddHabit?.setOnClickListener {
            findNavController().navigate(R.id.action_moviesListFragment_to_filtersFragment)
        }
    }
}