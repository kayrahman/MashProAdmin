package com.nkr.mashpro.ui.moviePlayer

import com.nkr.mashpro.model.Movie


sealed class MoviePlayerEvent {
    object OnFetchMovies : MoviePlayerEvent()
    data class OnDownloadMovie(val movie : Movie) : MoviePlayerEvent()
}