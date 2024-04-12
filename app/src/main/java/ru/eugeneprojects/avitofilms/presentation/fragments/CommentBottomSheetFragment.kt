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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
            commentDate.text = comment.date?.format(
                DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.LONG)
            ) ?: ""
            commentTitle.text = comment.title
            commentText.text = comment.review

        }
        comment.type?.let { setIndicatorColor(it) }
        setOnExitButtonClick()
    }

    private fun setIndicatorColor(commentType: String) {
        when (commentType) {
            Comment.TYPE_POSITIVE -> binding?.commentIndicator?.setBackgroundColor(requireContext().getColor(
                R.color.green))
            Comment.TYPE_NEGATIVE -> binding?.commentIndicator?.setBackgroundColor(requireContext().getColor(
                R.color.red))
            Comment.TYPE_NEUTRAL -> binding?.commentIndicator?.setBackgroundColor(requireContext().getColor(
                R.color.black))
        }
    }

    private fun setOnExitButtonClick() {
        binding?.closeButton?.setOnClickListener { findNavController().popBackStack() }
    }
}