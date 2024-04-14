package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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

    private var _binding: FragmentMoviesListBinding? = null
    private val binding: FragmentMoviesListBinding
        get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesPagingAdapter: MoviesPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater)
        return binding.root
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
                Toast.makeText(
                    context,
                    resources.getString(R.string.network_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpMoviesList() {

        moviesPagingAdapter = MoviesPagingAdapter()

        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMovies.adapter =
            moviesPagingAdapter.withLoadStateFooter(MoviesLoadStateAdapter())

        setOnMovieClick()

        initSwipeToRefresh(moviesPagingAdapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviesPagingAdapter.loadStateFlow.collect { loadStates ->
                    binding.swipeRefresh.isRefreshing =
                        loadStates.mediator?.refresh is LoadState.Loading
                }
            }
        }

        moviesPagingAdapter.addLoadStateListener { combinedLoadStates ->

            with(binding) {
                when (combinedLoadStates.refresh) {
                    is LoadState.Error -> {
                        recyclerViewMovies.isVisible = true
                        textViewStub.isVisible = false
                        progressBar.isVisible = false

                        Toast.makeText(
                            context,
                            resources.getString(R.string.toast_load_error_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    LoadState.Loading -> {
                        recyclerViewMovies.isVisible = false
                        textViewStub.isVisible = false
                        progressBar.isVisible = true
                    }

                    is LoadState.NotLoading -> {
                        if (combinedLoadStates.append.endOfPaginationReached && moviesPagingAdapter.itemCount == 0) {
                            recyclerViewMovies.isVisible = false
                            textViewStub.isVisible = true
                            progressBar.isVisible = false
                        } else {
                            recyclerViewMovies.isVisible = true
                            textViewStub.isVisible = false
                            progressBar.isVisible = false
                        }
                    }
                }
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
            val action =
                MoviesListFragmentDirections.actionMoviesListFragmentToMovieDescriptionFragment(
                    it.id
                )
            findNavController().navigate(action)
        }
    }

    private fun initSwipeToRefresh(adapter: MoviesPagingAdapter) {

        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun handleSearchChanges() {

        binding.searchEditText.doOnTextChanged { searchQuery, _, _, _ ->
            viewModel.setSearchQuery(searchQuery.toString())
        }
    }

    private fun handleScrollingToTopWhenSearching(adapter: MoviesPagingAdapter) =
        lifecycleScope.launch {
            getRefreshLoadStateFlow(adapter)
                .simpleScan(2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.recyclerViewMovies.scrollToPosition(0)
                    }
                }
        }

    private fun getRefreshLoadStateFlow(adapter: MoviesPagingAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private fun setUpFabButton() {
        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_moviesListFragment_to_filtersFragment)
        }
    }
}