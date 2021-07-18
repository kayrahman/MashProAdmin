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
   // val movieList = MutableLiveData<List<Movie>>()
    val movieListNew = MutableLiveData<List<Movie>>()
    val movieListSlide = MutableLiveData<List<Movie>>()
    val currentMovie = MutableLiveData<Movie>()
    val showPoster = MutableLiveData<Boolean>()

    fun handleEvent(event : MoviePlayerEvent){
        when(event){
            is MoviePlayerEvent.OnFetchMovies ->  fetchMovies()
            is MoviePlayerEvent.OnDownloadMovie -> downloadMovies(event.movie)
        }
    }

    private fun downloadMovies(movie: Movie) = viewModelScope.launch {
        val download_response = repo.downloadMovieFromRemote(movie)
        when(download_response){
            is Result.Success ->{
                showSnackBar.value = "Movie downloaded"
                Timber.i("download_status : Success")
            }
            is Result.Error ->{
                showSnackBar.value = "Movie download error"
                Timber.i("download_status : Error ${download_response.exception.toString()}")
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


    fun setCurrentMovie(movie : Movie){
        currentMovie.value = movie
    }

}