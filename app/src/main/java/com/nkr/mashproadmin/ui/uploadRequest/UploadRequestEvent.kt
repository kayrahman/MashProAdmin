package com.nkr.mashproadmin.ui.uploadRequest

sealed class UploadRequestEvent {
    object OnFetchPendingVideos : UploadRequestEvent()
    data class OnAcceptPendingVideos(val movie_uid : String) : UploadRequestEvent()
    data class OnRejectPendingVideos(val movie_uid : String) : UploadRequestEvent()
}