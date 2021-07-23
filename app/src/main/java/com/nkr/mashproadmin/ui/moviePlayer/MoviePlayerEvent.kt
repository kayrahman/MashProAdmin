package com.nkr.mashproadmin.ui.moviePlayer

import com.nkr.mashproadmin.model.Movie


sealed class MoviePlayerEvent {
    object OnFetchMovies : MoviePlayerEvent()
    data class OnDownloadMovie(val movie : Movie) : MoviePlayerEvent()
}