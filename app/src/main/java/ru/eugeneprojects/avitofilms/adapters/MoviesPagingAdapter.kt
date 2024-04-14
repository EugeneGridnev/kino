package ru.eugeneprojects.avitofilms.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.moviedescription.MovieCardInfo
import ru.eugeneprojects.avitofilms.databinding.ItemMovieLayoutBinding
import ru.eugeneprojects.avitofilms.utils.filterBlank
import java.util.Locale

class MoviesPagingAdapter :
    PagingDataAdapter<MovieCardInfo, MoviesPagingAdapter.MovieViewHolder>(MovieDiffCallBack()) {

    private var onItemClickListener: ((MovieCardInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
            ItemMovieLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movie = getItem(position) ?: return
        holder.bind(movie, onItemClickListener)
    }


    class MovieViewHolder(val binding: ItemMovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(movie: MovieCardInfo, onClickListener: ((MovieCardInfo) -> Unit)? = null) {

            with(binding) {
                Glide.with(itemView)
                    .load(movie.poster?.previewUrl)
                    .placeholder(R.drawable.no_movie_image_placeholder)
                    .into(itemViewMovieThumbnail)
                textViewMovieTitle.text = movie.name.filterBlank() ?: movie.enName.filterBlank()
                        ?: movie.alternativeName.filterBlank() ?: ""
                textViewMovieYear.text = movie.year.toString()
                textViewMovieCountry.text =
                    movie.countries.joinToString(", ") { it.name.filterBlank() ?: "" }
                textViewMovieGenre.text =
                    movie.genres.joinToString(", ") { it.name.filterBlank() ?: "" }
                textViewMovieRating.text = String.format(Locale.US, "%.1f", movie.rating?.kp)
                textViewMovieAgeRestriction.text = "${movie.ageRating}+"
            }
            itemView.setOnClickListener {
                onClickListener?.invoke(movie)
            }
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<MovieCardInfo>() {
        override fun areItemsTheSame(oldItem: MovieCardInfo, newItem: MovieCardInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieCardInfo, newItem: MovieCardInfo): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnItemClickListener(listener: (MovieCardInfo) -> Unit) {
        onItemClickListener = listener
    }
}