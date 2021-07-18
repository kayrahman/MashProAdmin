package com.nkr.mashpro.ui.account

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.model.FirebaseUserInfo
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.launch
import timber.log.Timber
import com.nkr.mashpro.repo.Result

class AccountViewModel(app:Application,val repo : IRepoDataSource) : BaseViewModel(app) {

    val userInfo = MutableLiveData<FirebaseUserInfo>()
    val userImg = MutableLiveData<String>()

    val movieList = MutableLiveData<List<Movie>>()

    fun handleEvent(event: AccountEvent){
        when(event){
            is AccountEvent.OnFetchUserInfo -> fetchUserInfo()
            is AccountEvent.OnUpdateUserImage -> UpdateUserImageToRemote(event.uri)
          //  is AccountEvent.OnFetchMovies -> fetchMovies()
        }
    }

/*
    private fun fetchMovies() = viewModelScope.launch{
        val response = repo.fetchMoviesFromRemote()
        when(response){
            is Result.Success ->
            {
                response.data.let {
                    movieList.value = it
                    Timber.i("movie_status : ${response.data}")
                }

            }
            is Result.Error -> {
                Timber.i("movie_status : ${response.exception}")
            }
        }
    }

    */

    private fun UpdateUserImageToRemote(uri: Uri) = viewModelScope.launch {
        showLoading.value = true
        val update_photo_response = repo.uploadUserThumbImageToRemote(uri)
        when(update_photo_response){
            is Result.Success -> {
                update_photo_response.data.let {
                    val update_user_image = repo.updateRemoteImageRef(it)
                    when(update_user_image){
                        is Result.Success -> {
                            showLoading.value = false
                            userImg.value = it
                        }
                        is Result.Error -> {
                            showLoading.value = false
                        }
                    }
                }


                Timber.i("upload_user_image : ${update_photo_response.data.toString()}")
            }
            is Result.Error -> {
                showLoading.value = false
                Timber.i("upload_user_image : ${update_photo_response.exception.toString()}")
            }
        }
    }

    private fun fetchUserInfo() = viewModelScope.launch {
        showLoading.value = true
        val response = repo.getUserInfoFromRemote()
        when(response){
            is Result.Success ->{
                response.data.let {
                    showLoading.value = false
                    userInfo.value = it
                    userImg.value = it?.img_url
                }

                Timber.i("user_info : ${response.data.toString()}")
            }
            is Result.Error ->{
                Timber.i("user_info : ${response.exception.toString()}")
                showLoading.value = false
            }
        }
    }


}