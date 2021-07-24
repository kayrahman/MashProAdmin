package com.nkr.mashproadmin.ui.uploadRequest

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.nkr.mashproadmin.repo.Result
import com.nkr.mashproadmin.util.KEY_ACCEPT
import com.nkr.mashproadmin.util.NODE_MOVIE_CONFIRMED
import com.nkr.mashproadmin.util.NODE_MOVIE_REJECTED

class PendingUploadRequestViewModel(app:Application,val repo : IRepoDataSource) : BaseViewModel(app) {

    val pendingMovies = MutableLiveData<List<Movie>>()

    fun handleEvent(event : UploadRequestEvent){
        when(event){
           is UploadRequestEvent.OnFetchPendingVideos -> fetchPendingMoviesFromRemote()
            is UploadRequestEvent.OnAcceptPendingVideos -> updateMovieStatus(event.movie_uid,NODE_MOVIE_CONFIRMED)
            is UploadRequestEvent.OnRejectPendingVideos -> updateMovieStatus(event.movie_uid,NODE_MOVIE_REJECTED)
        }
    }

    private fun updateMovieStatus(movie_uid:String,status : String) = viewModelScope.launch {
        val response = repo.updateMovieStatusInRemote(movie_uid,status)
        when(response){
            is Result.Success -> {
                if(status == NODE_MOVIE_CONFIRMED) {
                    showToast.value = "Movie has been approved"
                }else{
                    showToast.value = "Movie has been rejected"
                }
            }
            is Result.Error -> {
                showToast.value = "Error Occured"
            }
        }
    }

    private fun fetchPendingMoviesFromRemote() = viewModelScope.launch {
        val response = repo.fetchPendingMoviesFromRemote()
        when(response){
            is Result.Success -> {
                response.data.let {
                    if(it.isNotEmpty()){
                        pendingMovies.value = it
                    }
                }
            }
            is Result.Error -> {

            }
        }
    }
}