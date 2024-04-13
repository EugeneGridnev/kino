package ru.eugeneprojects.avitofilms.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.adapters.ActorsPagingAdapter
import ru.eugeneprojects.avitofilms.adapters.CommentsPagingAdapter
import ru.eugeneprojects.avitofilms.data.models.movieDescription.MovieInfo
import ru.eugeneprojects.avitofilms.databinding.FragmentMovieDescriptionBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MovieDescriptionViewModel
import ru.eugeneprojects.avitofilms.utils.filterBlank
import java.util.Locale

@AndroidEntryPoint
class MovieDescriptionFragment : Fragment() {

    private var _binding: FragmentMovieDescriptionBinding? = null
    private val binding: FragmentMovieDescriptionBinding
        get() = _binding!!

    private val args: MovieDescriptionFragmentArgs by navArgs()
    private val viewModel: MovieDescriptionViewModel by viewModels()

    private lateinit var commentsPagingAdapter: CommentsPagingAdapter
    private lateinit var actorsPagingAdapter: ActorsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDescriptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovie(args.movieId)
        setUpUi()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setUpUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.description.collect { loadStates ->
                    when (loadStates) {
                        MovieDescriptionViewModel.State.Loading -> {
                            binding.progressBar.isVisible = true
                            binding.wholeMovieDescription.isVisible = false
                        }

                        is MovieDescriptionViewModel.State.Movie -> {
                            binding.progressBar.isVisible = false
                            binding.wholeMovieDescription.isVisible = true
                            setUpData(loadStates.data)
                        }

                        is MovieDescriptionViewModel.State.Error -> {
                            val action =
                                MovieDescriptionFragmentDirections.actionMovieDescriptionFragmentToErrorFragment(
                                    args.movieId
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
        setUpActorsRecycler()
        setUpCommentsRecycler()
        setUpBackButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpData(movieInfo: MovieInfo) {

        binding.imageViewMovieImage.let {
            Glide.with(this)
                .load(movieInfo.poster.url)
                .placeholder(R.drawable.no_movie_image_placeholder)
                .into(it)
        }
        binding.apply {
            textViewMovieName.text = movieInfo.name.filterBlank() ?: movieInfo.enName.filterBlank()
                    ?: movieInfo.alternativeName.filterBlank() ?: ""
            textViewMovieDescription.text = movieInfo.description
            textViewMovieYear.text = movieInfo.year.toString()
            textViewMovieSlogan.text = movieInfo.slogan ?: ""
            textViewMovieRating.text = String.format(Locale.US, "%.1f", movieInfo.rating?.kp)
            textViewMovieLength.text = "${movieInfo.movieLength ?: ""}"
            textViewMovieAgeRating.text = "${movieInfo.ageRating ?: ""}"
            textViewMovieGenres.text =
                movieInfo.genres.joinToString(", ") { it.name.filterBlank() ?: "" }
            textViewMovieCountries.text =
                movieInfo.countries.joinToString(", ") { it.name.filterBlank() ?: "" }
            textViewMovieBudget.text =
                "${movieInfo.budget?.value ?: ""} ${movieInfo.budget?.currency ?: ""}"
        }
    }

    private fun setUpCommentsRecycler() {
        commentsPagingAdapter = CommentsPagingAdapter()

        binding.recyclerViewComments.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewComments.adapter = commentsPagingAdapter

        setOnCommentClick()
        observeComments()

        commentsPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && commentsPagingAdapter.itemCount < 1) {
                binding.recyclerViewComments.isVisible = false
                binding.textViewCommentsStub.isVisible = true
            } else {
                binding.recyclerViewComments.isVisible = true
                binding.textViewCommentsStub.isVisible = false
            }
        }
    }

    private fun observeComments() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.comments.collectLatest(commentsPagingAdapter::submitData)
            }
        }
    }

    private fun setOnCommentClick() {

        commentsPagingAdapter.setOnItemClickListener {
            val action =
                MovieDescriptionFragmentDirections.actionMovieDescriptionFragmentToCommentBottomSheetFragment(
                    it
                )
            findNavController().navigate(action)
        }
    }

    private fun setUpActorsRecycler() {
        actorsPagingAdapter = ActorsPagingAdapter()

        binding.recyclerViewActors.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
        binding.recyclerViewActors.adapter = actorsPagingAdapter

        observeActors()

        actorsPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && actorsPagingAdapter.itemCount < 1) {
                binding.recyclerViewActors.isVisible = false
                binding.textViewActorsStub.isVisible = true
            } else {
                binding.recyclerViewActors.isVisible = true
                binding.textViewActorsStub.isVisible = false
            }
        }
    }

    private fun observeActors() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actors.collectLatest(actorsPagingAdapter::submitData)
            }
        }
    }

    private fun setUpBackButton() {

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}