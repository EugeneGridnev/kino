package ru.eugeneprojects.avitofilms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.databinding.ItemMovieLayoutBinding

class MoviesPagingAdapter :
    PagingDataAdapter<Movie, MoviesPagingAdapter.MovieViewHolder>(MovieDiffCallBack()) {

    private var onItemClickListener: ((Movie) -> Unit)? = null

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

        fun bind(movie: Movie, onClickListener: ((Movie) -> Unit)? = null) {

            Glide.with(itemView)
                .load(movie.poster.previewUrl)
                .placeholder(R.drawable.no_movie_image_placeholder)
                .into(binding.itemViewMovieThumbnail)
            binding.textViewMovieTitle.text = movie.name ?: movie.enName ?: movie.alternativeName ?: ""
            binding.textViewMovieYear.text = movie.year.toString()
            binding.textViewMovieCountry.text = movie.countries.joinToString(", ") { it.name }
            binding.textViewMovieGenre.text = movie.genres.joinToString(", ") { it.name }
            binding.textViewMovieRating.text = movie.rating.kp.toString()
            itemView.setOnClickListener {
                onClickListener?.invoke(movie)
            }
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }
}