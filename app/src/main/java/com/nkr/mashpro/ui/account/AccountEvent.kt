package com.nkr.mashpro.ui.account

import android.net.Uri
import java.io.File


sealed class AccountEvent {

    object OnFetchUserInfo : AccountEvent()
    data class OnUpdateUserImage(val uri : Uri) : AccountEvent()
    object OnFetchMovies : AccountEvent()
}