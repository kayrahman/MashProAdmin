package com.nkr.mashpro.ui.userCredential

import android.net.Uri
import com.nkr.mashpro.ui.account.AccountEvent


sealed class UserEvent {
    data class OnUpdateUserType(val user_type : Int) : UserEvent()
    object OnFetchUserInfo : UserEvent()
    data class OnUpdateUserImage(val uri : Uri) : UserEvent()
    object OnFetchMovies : UserEvent()
}