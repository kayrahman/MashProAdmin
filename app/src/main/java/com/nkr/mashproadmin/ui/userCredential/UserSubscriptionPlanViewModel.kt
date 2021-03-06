package com.nkr.mashproadmin.ui.userCredential

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nkr.bazaranocustomer.base.NavigationCommand
import com.nkr.mashproadmin.base.BaseViewModel
import com.nkr.mashproadmin.model.FirebaseUserInfo
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.nkr.mashproadmin.repo.Result
import com.nkr.mashproadmin.ui.authentication.FirebaseUserLiveData
import com.nkr.mashproadmin.util.USER_SUBSCRIPTION_TYPE_FREE
import com.nkr.mashproadmin.util.USER_SUBSCRIPTION_TYPE_MONTHLY
import com.nkr.mashproadmin.util.USER_TYPE_CREATOR
import timber.log.Timber

class UserSubscriptionPlanViewModel(app:Application, val repo : IRepoDataSource) : BaseViewModel(app) {

    val userType = MutableLiveData<Int>(803)
    val isUserTypeUpdated = MutableLiveData<Boolean>(false)


    val userInfo = MutableLiveData<FirebaseUserInfo>()
    val userSubcriptionType = MutableLiveData<String>()
    val userImg = MutableLiveData<String>()
    val movieList = MutableLiveData<List<Movie>>()


    fun handleEvent(event: UserEvent){
        when(event){
            is UserEvent.OnUpdateUserType ->{
                updateUserType(event.user_type)
            }

            is UserEvent.OnFetchUserInfo -> fetchUserInfo()
            is UserEvent.OnUpdateUserImage -> UpdateUserImageToRemote(event.uri)
            is UserEvent.OnFetchMovies -> fetchMovies()
        }
    }


    private fun fetchMovies() = viewModelScope.launch{
        val response = repo.fetchOwnMoviesFromRemote()
        when(response){
            is Result.Success ->
            {
                response.data.let {
                    movieList.value = it
                    Timber.i("movie_status : ${response.data}")
                }

            }
            is Result.Error -> {
                Timber.i("movie_status : ${response.exception}")
            }
        }
    }


    private fun UpdateUserImageToRemote(uri: Uri) = viewModelScope.launch {
        showLoading.value = true
        val update_photo_response = repo.uploadUserThumbImageToRemote(uri)
        when(update_photo_response){
            is Result.Success -> {
                update_photo_response.data.let {
                    val update_user_image = repo.updateRemoteImageRef(it)
                    when(update_user_image){
                        is Result.Success -> {
                            showLoading.value = false
                            userImg.value = it
                        }
                        is Result.Error -> {
                            showLoading.value = false
                        }
                    }
                }


                Timber.i("upload_user_image : ${update_photo_response.data.toString()}")
            }
            is Result.Error -> {
                showLoading.value = false
                Timber.i("upload_user_image : ${update_photo_response.exception.toString()}")
            }
        }
    }

    private fun fetchUserInfo() = viewModelScope.launch {
        showLoading.value = true
        val response = repo.getUserInfoFromRemote()
        when(response){
            is Result.Success ->{
                response.data.let {
                    showLoading.value = false
                    userInfo.value = it
                    userImg.value = it?.img_url

                    if(userType.value == USER_TYPE_CREATOR ){
                        userSubcriptionType.value = "Creator"
                    }else{
                       userSubcriptionType.value = it.subscription_plan
                    }
                }

                Timber.i("user_info : ${response.data.toString()}")
            }
            is Result.Error ->{
                Timber.i("user_info : ${response.exception.toString()}")
                showLoading.value = false
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

    fun setupUserSubscriptionType(sub_plan_type: String){
        userSubcriptionType.value = sub_plan_type
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