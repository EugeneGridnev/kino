package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.filters.MovieFilters
import ru.eugeneprojects.avitofilms.data.models.filters.MovieSortType
import ru.eugeneprojects.avitofilms.data.models.filters.MovieTypeFilter
import ru.eugeneprojects.avitofilms.databinding.FragmentFilterBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.FilteredMovieListViewModel
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MoviesViewModel

@AndroidEntryPoint
class FiltersFragment : Fragment() {

    private val viewModel: FilteredMovieListViewModel by activityViewModels()
    private var binding: FragmentFilterBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUseFiltersButton()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setUpUseFiltersButton() {
        binding?.filterButton?.setOnClickListener {

            val type = when (binding?.tabMoviesTypeFilter?.selectedTabPosition) {
                1 -> MovieTypeFilter.MOVIES
                2 -> MovieTypeFilter.SERIES
                else -> MovieTypeFilter.ALL
            }
            val sort = when (binding?.tabSortMoviesFilter?.selectedTabPosition) {
                1 -> MovieSortType.COUNTRY
                2 -> MovieSortType.AGE_RATING
                else -> MovieSortType.YEAR
            }
            val rating = binding?.ratingSlider?.values?.let {
                it.first().toInt()..it.last().toInt()
            }

            val filters = MovieFilters(
                type = type,
                sort = sort,
                rating = rating ?: 6..10
            )
            viewModel.setFilter(filters)
            findNavController().navigate(R.id.action_filtersFragment_to_filteredMoviesListFragment)
        }
    }
}