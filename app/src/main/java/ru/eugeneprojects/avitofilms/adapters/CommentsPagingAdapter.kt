package ru.eugeneprojects.avitofilms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.comment.Comment
import ru.eugeneprojects.avitofilms.databinding.ItemCommentLayoutBinding

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