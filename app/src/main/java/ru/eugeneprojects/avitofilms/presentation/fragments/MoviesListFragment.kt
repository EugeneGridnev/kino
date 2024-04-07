package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.adapters.MoviesLoadStateAdapter
import ru.eugeneprojects.avitofilms.adapters.MoviesPagingAdapter
import ru.eugeneprojects.avitofilms.databinding.FragmentMoviesListBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MoviesViewModel

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var binding: FragmentMoviesListBinding? = null

    private lateinit var viewModel: MoviesViewModel
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

        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]

        setUpMoviesList()
        observeMovies()

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
    }

    private fun observeMovies() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMovies().collectLatest { pagingData ->
                    moviesPagingAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setOnMovieClick() {

        moviesPagingAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_moviesListFragment_to_movieFragment
            )
        }
    }
}