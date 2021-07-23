package com.nkr.mashproadmin.ui.downloads

sealed class DownloadEvent {
    object OnFetchDownloadedVideos : DownloadEvent()
}