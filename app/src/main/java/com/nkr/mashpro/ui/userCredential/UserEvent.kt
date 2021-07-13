package com.nkr.mashpro.ui.userCredential


sealed class UserEvent {
    data class OnUpdateUserType(val user_type : Int) : UserEvent()
}