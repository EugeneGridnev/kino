package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.adapters.MoviesLoadStateAdapter
import ru.eugeneprojects.avitofilms.adapters.MoviesPagingAdapter
import ru.eugeneprojects.avitofilms.databinding.FragmentFilteredMoviesListBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.FilteredMovieListViewModel
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MoviesViewModel

@AndroidEntryPoint
class FilteredMoviesListFragment : Fragment() {

    private var binding: FragmentFilteredMoviesListBinding? = null

    private val viewModel: FilteredMovieListViewModel by activityViewModels()
    private lateinit var moviesPagingAdapter: MoviesPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilteredMoviesListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMoviesList()
        observeMovies()
        setUpBackButton()

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

    private fun setUpBackButton() {
        binding?.backButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeMovies() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest(moviesPagingAdapter::submitData)
            }
        }
    }

    private fun setUpMoviesList() {

        moviesPagingAdapter = MoviesPagingAdapter()

        binding?.recyclerViewFilteredMovies?.layoutManager = LinearLayoutManager(activity)
        binding?.recyclerViewFilteredMovies?.adapter = moviesPagingAdapter.withLoadStateFooter(
            MoviesLoadStateAdapter()
        )

        setOnMovieClick()

        moviesPagingAdapter.addLoadStateListener { combinedLoadStates ->
            val refreshState = combinedLoadStates.refresh

            if (refreshState is LoadState.Error) {
                Toast.makeText(activity,resources.getString(R.string.toast_load_error_message), Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    delay(5000)
                    Toast.makeText(activity, "Подгружаем вам данные, не отключайтесь!", Toast.LENGTH_SHORT).show()
                    moviesPagingAdapter.retry()
                }
            }
        }

    }

    private fun setOnMovieClick() {

        moviesPagingAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_filteredMoviesListFragment_to_movieFragment
            )
        }
    }
}