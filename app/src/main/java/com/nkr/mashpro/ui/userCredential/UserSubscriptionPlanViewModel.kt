package com.nkr.mashpro.ui.userCredential

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashpro.base.BaseViewModel
import com.nkr.mashpro.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.nkr.mashpro.repo.Result
import com.nkr.mashpro.util.USER_SUBSCRIPTION_TYPE_FREE
import com.nkr.mashpro.util.USER_SUBSCRIPTION_TYPE_MONTHLY

class UserSubscriptionPlanViewModel(app:Application, val repo : IRepoDataSource) : BaseViewModel(app) {

    fun updateSubscriptionPlan(sub_plan_type:String) = viewModelScope.launch {
        showLoading.value = true
        val response = repo.updateUserSubscriptionPlanToRemote(sub_plan_type)
        when(response){
            is Result.Success ->{
                when(sub_plan_type){
                    USER_SUBSCRIPTION_TYPE_MONTHLY ->{
                        showLoading.value = false
                    }
                    USER_SUBSCRIPTION_TYPE_FREE -> {
                        showLoading.value = false
                        //go to home screen
                        val actionHome = UserSubscriptionPlanFragmentDirections.actionUserTypeFragmentToNavigationHome()
                        navigationCommand.value = NavigationCommand.To(actionHome)
                    }
                }
            }
            is Result.Error ->{
                showLoading.value = false
            }
        }
    }

}