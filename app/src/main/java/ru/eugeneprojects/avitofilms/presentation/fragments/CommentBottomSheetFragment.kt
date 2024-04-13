package ru.eugeneprojects.avitofilms.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.databinding.FragmentCommentBottomSheetBinding
import ru.eugeneprojects.avitofilms.utils.filterBlank
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CommentBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentBottomSheetBinding? = null
    private val binding: FragmentCommentBottomSheetBinding
        get() = _binding!!

    private val args: CommentBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setUpUi() {

        val comment = args.comment

        with(binding) {
            commentatorName.text = comment.author.filterBlank() ?: ""
            commentDate.text = comment.date?.format(
                DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.LONG)
            ) ?: ""
            commentTitle.text = comment.title.filterBlank() ?: ""
            commentText.text = comment.review.filterBlank() ?: ""

        }
        comment.type?.let { setIndicatorColor(it) }
        setOnExitButtonClick()
    }

    private fun setIndicatorColor(commentType: String) {
        when (commentType) {
            Comment.TYPE_POSITIVE -> binding.commentIndicator.setBackgroundColor(requireContext().getColor(
                R.color.green))
            Comment.TYPE_NEGATIVE -> binding.commentIndicator.setBackgroundColor(requireContext().getColor(
                R.color.red))
            Comment.TYPE_NEUTRAL -> binding.commentIndicator.setBackgroundColor(requireContext().getColor(
                R.color.black))
        }
    }

    private fun setOnExitButtonClick() {
        binding.closeButton.setOnClickListener { findNavController().popBackStack() }
    }
}