package com.nkr.mashpro.repo.remote

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.nkr.mashpro.model.FirebaseMovieInfo
import com.nkr.mashpro.model.FirebaseUserInfo
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.model.MovieLocationInfo
import kotlinx.coroutines.flow.Flow
import com.nkr.mashpro.repo.Result


interface IRemoteDataSource {

    //------------------NOTIFICAITON------------------//
    suspend fun getRegistrationToken() : Result<ArrayList<String>>
    suspend fun setFCMRegistrationToken(tokens:ArrayList<String>) : Result<Unit>


    //------------------ USER ------------------------------//
    suspend fun setupUserInRemote() : Result<Unit>
    suspend fun updateUserSubscriptionPlan(sub_plan : String) : Result<Unit>
    suspend fun getUserInfo() : Result<FirebaseUserInfo>

    //storage
    suspend fun uploadUserThumbImage(uri: Uri) : Result<String>
    suspend fun updateImageRef(img_ref : String) : Result<Unit>

    //upload
    suspend fun uploadVideoInfo(uri:Uri) : Result<MovieLocationInfo>
    suspend fun uploadMovieThumbImage(uri:Uri) : Result<String>
    suspend fun uploadMovieInfo(movie :FirebaseMovieInfo) : Result<Unit>

    //movie
    suspend fun fetchMovies():Result<List<Movie>>
    suspend fun downloadMovieToLocalFile(movie : String) : Result<String>

    //search
    suspend fun fetchMoviesBySearch(queryString: String): Result<List<Movie>>

}









