package com.nkr.mashpro.ui.moviePlayer


sealed class MoviePlayerEvent {
    object OnFetchMovies : MoviePlayerEvent()
    data class OnDownloadMovie(val download_url : String) : MoviePlayerEvent()
}