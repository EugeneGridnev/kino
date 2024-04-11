package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.databinding.FragmentCommentBottomSheetBinding

class CommentBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: FragmentCommentBottomSheetBinding? = null
    private val args: CommentBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBottomSheetBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun setUpUi() {

        val comment = args.comment

        binding?.apply {
            commentatorName.text = comment.author
            commentDate.text = comment.date
            commentTitle.text = comment.title
            commentText.text = comment.review

        }
        setIndicatorColor(comment.type)
        setOnExitButtonClick()
    }

    private fun setIndicatorColor(commentType: String) {
        when (commentType) {
            "Позитивный" -> binding?.commentIndicator?.setBackgroundColor(requireContext().getColor(
                R.color.green))
            "Негативный" -> binding?.commentIndicator?.setBackgroundColor(requireContext().getColor(
                R.color.red))
        }
    }

    private fun setOnExitButtonClick() {
        binding?.closeButton?.setOnClickListener { findNavController().popBackStack() }
    }
}