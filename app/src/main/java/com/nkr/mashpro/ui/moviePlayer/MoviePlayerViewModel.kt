package com.nkr.mashpro.ui.moviePlayer

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.IRepoDataSource
import com.nkr.mashpro.repo.Result
import com.nkr.mashpro.ui.home.MovieListAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

class MoviePlayerViewModel(app: Application, val repo : IRepoDataSource) : BaseViewModel(app) {
    val adapter = MovieListHorizontalAdapter()
    val movieList = MutableLiveData<List<Movie>>()
    val currentMovie = MutableLiveData<Movie>()
    val showPoster = MutableLiveData<Boolean>()

    fun handleEvent(event : MoviePlayerEvent){
        when(event){
            is MoviePlayerEvent.OnFetchMovies ->  fetchMovies()
            is MoviePlayerEvent.OnDownloadMovie -> downloadMovies(event.download_url)
        }
    }

    private fun downloadMovies(downloadUrl: String) = viewModelScope.launch {
        val download_response = repo.downloadMovieFromRemote(downloadUrl)
        when(download_response){
            is Result.Success ->{
                Timber.i("download_status : Success")
            }
            is Result.Error ->{
                Timber.i("download_status : Error ${download_response.exception.toString()}")
            }
        }
    }


    private fun fetchMovies() = viewModelScope.launch{
        val response = repo.fetchMoviesFromRemote()
        when(response){
            is Result.Success ->
            {
                response.data.let {
                    it.minus(currentMovie.value)
                    movieList.value = it
                    Timber.i("movie_status : ${response.data}")
                }

            }
            is Result.Error -> {
                Timber.i("movie_status : ${response.exception}")
            }
        }
    }

    fun setCurrentMovie(movie : Movie){
        currentMovie.value = movie
    }

}