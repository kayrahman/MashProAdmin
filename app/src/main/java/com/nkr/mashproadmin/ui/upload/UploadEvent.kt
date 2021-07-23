package com.nkr.mashproadmin.ui.upload

sealed class UploadEvent {
    object OnUploadMovieInfo : UploadEvent()
}