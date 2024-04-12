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
import ru.eugeneprojects.avitofilms.databinding.ErrorFragmentBinding
import ru.eugeneprojects.avitofilms.databinding.FragmentFilterBinding
import ru.eugeneprojects.avitofilms.presentation.viewmodels.FilteredMovieListViewModel
import ru.eugeneprojects.avitofilms.presentation.viewmodels.MoviesViewModel
import ru.eugeneprojects.avitofilms.utils.Constants

@AndroidEntryPoint
class FiltersFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding
        get() = _binding!!

    private val viewModel: FilteredMovieListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFilters()
        setUpDefaultFilter()
        setUpUseFiltersButton()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpFilters() {
        binding.apply {
            when (viewModel.filters.value?.type) {
                MovieTypeFilter.ALL -> tabMoviesTypeFilter.getTabAt(0)?.select()
                MovieTypeFilter.MOVIES -> tabMoviesTypeFilter.getTabAt(1)?.select()
                MovieTypeFilter.SERIES -> tabMoviesTypeFilter.getTabAt(2)?.select()
                null -> tabMoviesTypeFilter.getTabAt(0)?.select()
            }

            when (viewModel.filters.value?.sort) {
                MovieSortType.YEAR -> tabSortMoviesFilter.getTabAt(0)?.select()
                MovieSortType.COUNTRY -> tabSortMoviesFilter.getTabAt(1)?.select()
                MovieSortType.AGE_RATING -> tabSortMoviesFilter.getTabAt(2)?.select()
                null -> tabMoviesTypeFilter.getTabAt(0)?.select()

            }

            ratingSlider.setValues(
                viewModel.filters.value?.rating?.first?.toFloat(),
                viewModel.filters.value?.rating?.last?.toFloat()
            )
        }
    }

    private fun setUpDefaultFilter() {

        binding.defaultFilterButton.setOnClickListener {
            val defaultFilters = Constants.DEFAULT_FILTERS

            binding.apply {
                when (defaultFilters.type) {
                    MovieTypeFilter.ALL -> tabMoviesTypeFilter.getTabAt(0)?.select()
                    MovieTypeFilter.MOVIES -> tabMoviesTypeFilter.getTabAt(1)?.select()
                    MovieTypeFilter.SERIES -> tabMoviesTypeFilter.getTabAt(2)?.select()
                }

                when (defaultFilters.sort) {
                    MovieSortType.YEAR -> tabSortMoviesFilter.getTabAt(0)?.select()
                    MovieSortType.COUNTRY -> tabSortMoviesFilter.getTabAt(1)?.select()
                    MovieSortType.AGE_RATING -> tabSortMoviesFilter.getTabAt(2)?.select()
                }

                ratingSlider.setValues(
                    defaultFilters.rating.first.toFloat(),
                    defaultFilters.rating.last.toFloat()
                )
            }
            viewModel.setFilter(defaultFilters)
        }
    }

    private fun setUpUseFiltersButton() {

        binding.filterButton.setOnClickListener {

            val type = when (binding.tabMoviesTypeFilter.selectedTabPosition) {
                1 -> MovieTypeFilter.MOVIES
                2 -> MovieTypeFilter.SERIES
                else -> MovieTypeFilter.ALL
            }
            val sort = when (binding.tabSortMoviesFilter.selectedTabPosition) {
                1 -> MovieSortType.COUNTRY
                2 -> MovieSortType.AGE_RATING
                else -> MovieSortType.YEAR
            }
            val rating = binding.ratingSlider.values.let {
                it.first().toInt()..it.last().toInt()
            }

            val filters = MovieFilters(
                type = type,
                sort = sort,
                rating = rating
            )
            viewModel.setFilter(filters)
            findNavController().navigate(R.id.action_filtersFragment_to_filteredMoviesListFragment)
        }
    }
}