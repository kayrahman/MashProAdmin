package com.nkr.mashpro.ui.moviePlayer


sealed class MoviePlayerEvent {
    object OnFetchMovies : MoviePlayerEvent()
}