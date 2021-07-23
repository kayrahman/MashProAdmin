package com.nkr.mashproadmin.ui.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nkr.mashproadmin.R
import com.nkr.mashproadmin.databinding.ItemListLocalMovieBinding
import com.nkr.mashproadmin.model.Movie

class DownloadedMovieListAdapter : ListAdapter<Movie,DownloadedMovieListAdapter.HomeItemViewHolder>(DiffCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        val binding: ItemListLocalMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_local_movie, parent, false
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

    class HomeItemViewHolder(val binding: ItemListLocalMovieBinding) :
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