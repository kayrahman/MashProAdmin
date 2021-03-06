package com.nkr.mashproadmin.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.nkr.mashproadmin.repo.Result
import timber.log.Timber

class HomeViewModel(app:Application,val repo : IRepoDataSource) : BaseViewModel(app) {

    val adapter = MovieListAdapter()
    val movieListNew = MutableLiveData<List<Movie>>()
    val movieListSlide = MutableLiveData<List<Movie>>()

    fun handleEvent(event: HomeEvent){
        when(event){
            is HomeEvent.OnFetchMovies -> {
                fetchMovies()
            }
        }
    }

    private fun fetchMovies() = viewModelScope.launch{
        val response = repo.fetchNewMoviesFromRemote()
        when(response){
            is Result.Success ->
            {
                response.data.let {
                    movieListNew.value = it
                    Timber.i("movie_status_new : ${response.data.size}")
                }

            }
            is Result.Error -> {
                Timber.i("movie_status_new : ${response.exception}")
            }
        }

        val response_slide_movies = repo.fetchSlideMoviesFromRemote()
        when(response_slide_movies){
            is Result.Success ->
            {
                response_slide_movies.data.let {
                    movieListSlide.value = it
                    Timber.i("movie_status_slide : ${response_slide_movies.data.size}")
                }

            }
            is Result.Error -> {
                Timber.i("movie_status_slide : ${response_slide_movies.exception}")
            }
        }



    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}