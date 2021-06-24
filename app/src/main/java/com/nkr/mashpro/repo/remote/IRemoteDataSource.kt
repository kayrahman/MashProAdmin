package com.nkr.mashpro.repo.remote

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.nkr.mashpro.model.FirebaseUserInfo
import kotlinx.coroutines.flow.Flow
import com.nkr.mashpro.repo.Result


interface IRemoteDataSource {

    //------------------NOTIFICAITON------------------//
    suspend fun getRegistrationToken() : Result<ArrayList<String>>
    suspend fun setFCMRegistrationToken(tokens:ArrayList<String>) : Result<Unit>


    //------------------ USER ------------------------------//
    suspend fun setupUserInRemote() : Result<Unit>
    suspend fun getUserInfo() : Result<FirebaseUserInfo>

    //upload
    suspend fun uploadVideoInfo(uri:Uri) : Result<String>

}