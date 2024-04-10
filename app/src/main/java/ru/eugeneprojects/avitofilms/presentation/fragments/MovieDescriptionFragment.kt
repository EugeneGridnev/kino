package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.eugeneprojects.avitofilms.databinding.FragmentMovieDescriptionBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MovieDescriptionViewModel

@AndroidEntryPoint
class MovieDescriptionFragment : Fragment() {
    private var binding: FragmentMovieDescriptionBinding? = null
    private val args: MovieDescriptionFragmentArgs by navArgs()
    private val viewModel: MovieDescriptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDescriptionBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO передать id
        viewModel.getMovie(args.movieId)
        setUpUi()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun setUpUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.description.collect { loadStates ->
                    when (loadStates) {
                        MovieDescriptionViewModel.State.Loading -> {}
                        is MovieDescriptionViewModel.State.Movie -> {
                            loadStates.data.let {
                                binding?.textViewMovieName?.text = it.name
                            }
                        }
                    }
                }
            }
        }
    }
}