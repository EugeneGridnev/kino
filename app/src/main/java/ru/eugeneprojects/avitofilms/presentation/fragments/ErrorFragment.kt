package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.eugeneprojects.avitofilms.databinding.ErrorFragmentBinding

class ErrorFragment: Fragment() {

    private var _binding: ErrorFragmentBinding? = null
    private val binding: ErrorFragmentBinding
        get() = _binding!!

    private val args: ErrorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ErrorFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnRetryClick()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setOnRetryClick() {
        val action = ErrorFragmentDirections.actionErrorFragmentToMovieDescriptionFragment(args.movieId)
        binding.repeatButton.setOnClickListener { findNavController().navigate(action) }
    }
}