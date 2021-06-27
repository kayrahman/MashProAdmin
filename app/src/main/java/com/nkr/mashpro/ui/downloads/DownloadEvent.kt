package com.nkr.mashpro.ui.downloads

sealed class DownloadEvent {
    object OnFetchDownloadedVideos : DownloadEvent()
}