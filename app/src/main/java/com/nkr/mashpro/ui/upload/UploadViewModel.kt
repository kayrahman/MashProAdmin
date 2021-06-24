package com.nkr.mashpro.ui.upload

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.bazaranocustomer.util.SingleLiveEvent
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.model.FirebaseMovie
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.launch
import timber.log.Timber
import com.nkr.mashpro.repo.Result

class UploadViewModel(app: Application, val repo: IRepoDataSource) : BaseViewModel(app) {

    val movieTitle = MutableLiveData<String>()
    val movieYear = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val selectedMovieType = MutableLiveData<String>()
    val movieTypes = mutableListOf<String>()

    val videoUri = MutableLiveData<Uri>()
    val thumbUri = MutableLiveData<Uri>()

    val filePath = MutableLiveData<String>("")

    val isUploadSuccessful = SingleLiveEvent<Boolean>()


    init {
        movieTypes.add("Slide")
        movieTypes.add("New")
    }


    fun handleEvent(event: UploadEvent) {
        when (event) {
            is UploadEvent.OnUploadMovieInfo -> {
                if (validateMovieInfo()) {
                    uploadVideoIntoRemote()
                } else {
                    showSnackBar.value = "Do not leave empty fields"
                }
            }
        }
    }

    fun uploadVideoIntoRemote() = viewModelScope.launch {
        Timber.i("video_uri_upload : ${videoUri.value.toString()}")
        videoUri.value?.let {
            showLoading.value = true
            val response = repo.uploadVideoInfoToRemote(it)
            when (response) {
                is Result.Success -> {
                    // upload video info
                    val thumbnail_response = repo.uploadMovieThumbImageToRemote(thumbUri.value!!)
                    when (thumbnail_response) {
                        is Result.Success -> {
                            val title = movieTitle.value.toString()
                            val year = movieYear.value.toString()
                            val desc = movieDescription.value.toString()
                            val type = selectedMovieType.value.toString()
                            val movie_info = FirebaseMovie(
                                movie_title = title, movie_year = year, description = desc,
                                type = type
                            )

                            val upload_movie_response = repo.uploadMovieInfoIntoRemote(movie_info)
                            when (upload_movie_response) {
                                is Result.Success -> {
                                    isUploadSuccessful.value = true
                                    showLoading.value = false
                                }
                                is Result.Error -> {
                                    showSnackBar.value = "Error uploading movie"
                                    showLoading.value = false
                                }
                            }

                        }

                        is Result.Error -> {
                            showSnackBar.value = "Error uploading thumb image"
                            showLoading.value = false
                        }
                    }




                    Timber.i("video_uri_upload : success - ${response.data}")

                }
                is Result.Error -> {
                    showSnackBar.value = "Error uploading video file"
                    Timber.i("video_uri_upload : ${response.exception.toString()}")
                    showLoading.value = false
                }
            }
        }

    }

    fun validateMovieInfo(): Boolean {
        val title = movieTitle.value.toString()
        val year = movieYear.value.toString()
        val desc = movieDescription.value.toString()
        val type = selectedMovieType.value.toString()

        return (title.isNotEmpty() && year.isNotEmpty()
                && desc.isNotEmpty() && type.isNotEmpty()
                && videoUri.value != null && thumbUri.value != null)

        Timber.i("movie_info : title:$title, year : $year, desc : $desc, type:$type")
    }

    fun setVideoUri(video_uri: Uri) {
        videoUri.value = video_uri
    }

    fun setImageUri(image_uri: Uri) {
        thumbUri.value = image_uri
    }

    fun setFilePath(video_uri: String) {
        filePath.value = video_uri
    }

}