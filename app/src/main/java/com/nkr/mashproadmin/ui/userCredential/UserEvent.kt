package com.nkr.mashproadmin.ui.userCredential

import android.net.Uri


sealed class UserEvent {
    data class OnUpdateUserType(val user_type : Int) : UserEvent()
    object OnFetchUserInfo : UserEvent()
    data class OnUpdateUserImage(val uri : Uri) : UserEvent()
    object OnFetchMovies : UserEvent()
}