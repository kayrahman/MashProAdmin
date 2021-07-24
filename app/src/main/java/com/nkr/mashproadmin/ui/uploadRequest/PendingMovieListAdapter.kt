package com.nkr.mashproadmin.ui.uploadRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nkr.mashproadmin.R
import com.nkr.mashproadmin.databinding.ItemListLocalMovieBinding
import com.nkr.mashproadmin.databinding.ItemListUploadRequestBinding
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.util.KEY_ACCEPT
import com.nkr.mashproadmin.util.KEY_REJECT

class PendingMovieListAdapter : ListAdapter<Movie,PendingMovieListAdapter.PendingMovieItemViewHolder>(DiffCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingMovieItemViewHolder {
        val binding: ItemListUploadRequestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_upload_request, parent, false
        )
        return PendingMovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingMovieItemViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)

        holder.binding.ivThumbnail.setOnClickListener {
            listener.onMovieItemClicked(movie)
        }

        holder.binding.btnAccept.setOnClickListener {
            listener.onStatusBtnClick(movie.uid,KEY_ACCEPT)
        }

        holder.binding.btnReject.setOnClickListener {
            listener.onStatusBtnClick(movie.uid,KEY_REJECT)
        }

    }

    class PendingMovieItemViewHolder(val binding: ItemListUploadRequestBinding) :
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
    class MovieItemClickListener(val listener: (Movie) -> Unit, val status_listener : (String,Int) -> Unit) {
        fun onMovieItemClicked(movie : Movie) = listener(movie)
        fun onStatusBtnClick(movie_uid:String,status:Int) = status_listener(movie_uid,status)

    }


}