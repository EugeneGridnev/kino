package ru.eugeneprojects.avitofilms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.databinding.ItemCommentLayoutBinding
import ru.eugeneprojects.avitofilms.utils.filterBlank
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CommentsPagingAdapter :
    PagingDataAdapter<Comment, CommentsPagingAdapter.CommentViewHolder>(CommentDiffCallBack()) {

    private var onItemClickListener: ((Comment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {

        return CommentViewHolder(
            ItemCommentLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comment = getItem(position) ?: return
        holder.bind(comment, onItemClickListener)
    }


    class CommentViewHolder(val binding: ItemCommentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment, onClickListener: ((Comment) -> Unit)? = null) {
            with(binding) {
                when (comment.type) {
                    Comment.TYPE_POSITIVE -> commentIndicator.setBackgroundColor(
                        itemView.context.getColor(
                            R.color.green
                        )
                    )

                    Comment.TYPE_NEGATIVE -> commentIndicator.setBackgroundColor(
                        itemView.context.getColor(
                            R.color.red
                        )
                    )

                    Comment.TYPE_NEUTRAL -> commentIndicator.setBackgroundColor(
                        itemView.context.getColor(
                            R.color.black
                        )
                    )

                    null -> commentIndicator.setBackgroundColor(
                        itemView.context.getColor(
                            R.color.black
                        )
                    )
                }

                commentatorName.text = comment.author.filterBlank() ?: ""
                commentDate.text = comment.date?.format(
                    DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.LONG)
                ) ?: ""
                commentTitle.text = comment.title.filterBlank() ?: ""
                commentText.text = comment.review.filterBlank() ?: ""
            }
            itemView.setOnClickListener {
                onClickListener?.invoke(comment)
            }
        }
    }

    class CommentDiffCallBack : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnItemClickListener(listener: (Comment) -> Unit) {
        onItemClickListener = listener
    }
}