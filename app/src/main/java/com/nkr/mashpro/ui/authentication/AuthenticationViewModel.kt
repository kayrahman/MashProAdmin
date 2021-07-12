package com.nkr.mashpro.ui.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.launch
import timber.log.Timber
import com.nkr.mashpro.repo.Result

class AuthenticationViewModel(app:Application,val repo:IRepoDataSource) : BaseViewModel(app) {


    fun checkIfUserExistsInRemote() = viewModelScope.launch {
        // If it's user's first time then create an entry of user into the firestore db.
        // also insert into local db to reduce the network calls.
        // then we'll check the local db if user exists from 2nd call.
        Timber.i("checking_user : true")
        showLoading.value = true
        val response = repo.setupUserInRemote()
        when(response){
            is Result.Success -> {

                val user_response = repo.getUserInfoFromRemote()
                when(user_response){
                    is Result.Success -> {
                        showLoading.value = false
                        val user = user_response.data

                        if(user.user_type != 0) {
                            if (user.subscription_plan.isNotEmpty()) {
                                val actionHome =
                                    AuthenticationFragmentDirections.actionAuthenticationFragmentToNavigationHome()
                                navigationCommand.value = NavigationCommand.To(actionHome)

                            } else {
                                val actionChoosePlan =
                                    AuthenticationFragmentDirections.actionAuthenticationFragmentToUserTypeFragment()
                                navigationCommand.value = NavigationCommand.To(actionChoosePlan)
                            }

                        }else{
                            val actionUsertype = AuthenticationFragmentDirections.actionAuthenticationFragmentToUserTypeFragment2()
                            navigationCommand.value = NavigationCommand.To(actionUsertype)
                        }
                    }

                    is Result.Error ->{

                    }
                }



                Timber.i("setup_user : success")
            }
            is Result.Error->{
                showLoading.value = false
                // showSnackBar.value = response.exception.toString()
                Timber.i("setup_user : ${response.exception.toString()}")
            }
        }


    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }


}