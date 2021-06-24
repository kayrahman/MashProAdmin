package com.nkr.mashpro.ui.upload

sealed class UploadEvent {
    object OnUploadMovieInfo : UploadEvent()
}