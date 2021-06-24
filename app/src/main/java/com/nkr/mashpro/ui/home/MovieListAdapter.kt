package com.nkr.mashpro.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nkr.mashpro.R
import com.nkr.mashpro.databinding.ItemListMovieBinding
import com.nkr.mashpro.model.Movie

class MovieListAdapter : ListAdapter<Movie,MovieListAdapter.HomeItemViewHolder>(DiffCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        val binding: ItemListMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_movie, parent, false
        )
        return HomeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

    }

    class HomeItemViewHolder(val binding: ItemListMovieBinding) :
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



    lateinit var  listener : ProductItemClickListener
    class ProductItemClickListener(val listener: (String) -> Unit) {
        fun onProductItemClicked(product_uid:String) = listener(product_uid)

    }


}