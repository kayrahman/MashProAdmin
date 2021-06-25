package com.nkr.mashpro.ui.moviePlayer

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nkr.mashpro.R
import com.nkr.mashpro.databinding.ItemListMovieBinding
import com.nkr.mashpro.databinding.ItemListMovieHorizontalBinding
import com.nkr.mashpro.model.Movie

class MovieListHorizontalAdapter : ListAdapter<Movie,MovieListHorizontalAdapter.HomeItemViewHolder>(DiffCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        val binding: ItemListMovieHorizontalBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_movie_horizontal, parent, false
        )
        return HomeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)

        holder.binding.ivThumbnail.setOnClickListener {
            listener.onMovieItemClicked(movie)
        }

    }

    class HomeItemViewHolder(val binding: ItemListMovieHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.movie = item
            binding.executePendingBindings()
        }
    }


    class DiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.uid == newItem.uid
        }
    }


    lateinit var  listener : MovieItemClickListener
    class MovieItemClickListener(val listener: (Movie) -> Unit) {
        fun onMovieItemClicked(movie : Movie) = listener(movie)

    }


}