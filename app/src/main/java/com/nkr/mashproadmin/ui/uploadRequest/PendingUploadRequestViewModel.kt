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

class PendingUploadRequestViewModel(app:Application,val repo : IRepoDataSource) : BaseViewModel(app) {

    val pendingMovies = MutableLiveData<List<Movie>>()

    fun handleEvent(event : UploadRequestEvent){
        when(event){
           is UploadRequestEvent.OnFetchPendingVideos -> fetchPendingMoviesFromRemote()
        }
    }

    private fun fetchPendingMoviesFromRemote() = viewModelScope.launch {
        val response = repo.fetchNewMoviesFromRemote()
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