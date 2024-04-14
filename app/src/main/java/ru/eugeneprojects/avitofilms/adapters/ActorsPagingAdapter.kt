package ru.eugeneprojects.avitofilms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eugeneprojects.avitofilms.R
import ru.eugeneprojects.avitofilms.data.models.actor.Actor
import ru.eugeneprojects.avitofilms.databinding.ItemActorLayoutBinding
import ru.eugeneprojects.avitofilms.utils.filterBlank

class ActorsPagingAdapter :
    PagingDataAdapter<Actor, ActorsPagingAdapter.ActorViewHolder>(ActorDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {

        return ActorViewHolder(
            ItemActorLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {

        val actor = getItem(position) ?: return
        holder.bind(actor)
    }


    class ActorViewHolder(val binding: ItemActorLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Actor) {
            Glide.with(itemView)
                .load(actor.photo)
                .placeholder(R.drawable.no_person_image_placeholder)
                .into(binding.itemViewActorThumbnail)
            binding.textViewActorName.text =
                actor.name.filterBlank() ?: actor.enName.filterBlank() ?: ""
        }
    }

    class ActorDiffCallBack : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem == newItem
        }
    }

}