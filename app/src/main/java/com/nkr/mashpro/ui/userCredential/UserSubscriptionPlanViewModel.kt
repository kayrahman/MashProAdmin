package com.nkr.mashpro.ui.userCredential

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.nkr.mashpro.repo.Result
import com.nkr.mashpro.util.USER_SUBSCRIPTION_TYPE_FREE
import com.nkr.mashpro.util.USER_SUBSCRIPTION_TYPE_MONTHLY
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.inject

class UserSubscriptionPlanViewModel(app:Application, val repo : IRepoDataSource) : BaseViewModel(app) {

    val userType = MutableLiveData<Int>(803)
    val isUserTypeUpdated = MutableLiveData<Boolean>(false)

    fun handleEvent(event: UserEvent){
        when(event){
            is UserEvent.OnUpdateUserType ->{
                updateUserType(event.user_type)
            }
        }
    }

    private fun updateUserType(userType: Int) = viewModelScope.launch{
        val response = repo.updateRemoteUserType(userType)
        when(response){
            is Result.Success -> {
                setupUserType(userType)
                isUserTypeUpdated.value= true
            }

            is Result.Error -> {
                isUserTypeUpdated.value= false
            }
        }

    }

    fun updateSubscriptionPlan(sub_plan_type:String) = viewModelScope.launch {
        showLoading.value = true
        val response = repo.updateUserSubscriptionPlanToRemote(sub_plan_type)
        when(response){
            is Result.Success ->{
                when(sub_plan_type){
                    USER_SUBSCRIPTION_TYPE_MONTHLY ->{
                        goToHomeScreen()
                        showLoading.value = false
                    }
                    USER_SUBSCRIPTION_TYPE_FREE -> {
                        goToHomeScreen()
                        showLoading.value = false

                    }
                }
            }
            is Result.Error ->{
                showLoading.value = false
            }
        }
    }


    fun goToHomeScreen(){
        //go to home screen
        val actionHome = UserSubscriptionPlanFragmentDirections.actionUserTypeFragmentToNavigationHome()
        navigationCommand.value = NavigationCommand.To(actionHome)
    }


    fun setupUserType(user_type : Int){
        userType.value = user_type
    }

}