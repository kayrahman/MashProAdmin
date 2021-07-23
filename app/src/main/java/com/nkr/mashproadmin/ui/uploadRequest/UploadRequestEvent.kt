package com.nkr.mashproadmin.ui.uploadRequest

sealed class UploadRequestEvent {
    object OnFetchPendingVideos : UploadRequestEvent()
}