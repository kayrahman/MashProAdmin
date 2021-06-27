package com.nkr.mashpro.ui.downloads

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.nkr.mashpro.repo.Result
import timber.log.Timber

class DownloadsViewModel(app:Application,val repo:IRepoDataSource) : BaseViewModel(app) {

    fun handleEvent(event: DownloadEvent){
        when(event){
            is DownloadEvent.OnFetchDownloadedVideos -> fetchDowloadedVideosFromLocalDb()
        }
    }

    private fun fetchDowloadedVideosFromLocalDb() =viewModelScope.launch {
        val downloaded_video_response = repo.getDownloadedMoviesFromLocalDb()
        when(downloaded_video_response){
            is Result.Success ->{
                Timber.i("downloaded_videos : ${downloaded_video_response.data.toString()}")
            }
            is Result.Error ->{
                Timber.i("downloaded_videos : ${downloaded_video_response.exception.toString()}")
            }
        }
    }
}