package com.nkr.mashpro.ui.home


sealed class HomeEvent {
    object OnFetchMovies : HomeEvent()
}