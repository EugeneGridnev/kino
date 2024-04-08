package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.eugeneprojects.avitofilms.databinding.FragmentFilteredMoviesListBinding

class FilteredMoviesListFragment : Fragment() {

    private var binding: FragmentFilteredMoviesListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilteredMoviesListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBackButton()
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
}