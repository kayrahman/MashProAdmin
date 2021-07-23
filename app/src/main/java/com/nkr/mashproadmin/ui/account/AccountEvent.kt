package com.nkr.mashproadmin.ui.account

import android.net.Uri


sealed class AccountEvent {

    object OnFetchUserInfo : AccountEvent()
    data class OnUpdateUserImage(val uri : Uri) : AccountEvent()
    object OnFetchMovies : AccountEvent()
}