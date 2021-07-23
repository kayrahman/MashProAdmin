package com.nkr.mashproadmin.repo.remote

import android.net.Uri
import com.nkr.mashproadmin.model.FirebaseMovieInfo
import com.nkr.mashproadmin.model.FirebaseUserInfo
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.model.MovieLocationInfo
import com.nkr.mashproadmin.repo.Result


interface IRemoteDataSource {

    //------------------NOTIFICAITON------------------//
    suspend fun getRegistrationToken() : Result<ArrayList<String>>
    suspend fun setFCMRegistrationToken(tokens:ArrayList<String>) : Result<Unit>


    //------------------ USER ------------------------------//
    suspend fun setupUserInRemote() : Result<Unit>
    suspend fun updateUserSubscriptionPlan(sub_plan : String) : Result<Unit>
    suspend fun getUserInfo() : Result<FirebaseUserInfo>
    suspend fun updateUserType(user_type:Int) : Result<Unit>

    //storage
    suspend fun uploadUserThumbImage(uri: Uri) : Result<String>
    suspend fun updateImageRef(img_ref : String) : Result<Unit>

    //upload
    suspend fun uploadVideoInfo(uri:Uri) : Result<MovieLocationInfo>
    suspend fun uploadMovieThumbImage(uri:Uri) : Result<String>
    suspend fun uploadMovieInfo(movie :FirebaseMovieInfo) : Result<Unit>

    //movie
    suspend fun fetchNewMovies():Result<List<Movie>>
    suspend fun fetchSlideMovies():Result<List<Movie>>
    suspend fun fetchOwnUploadedMovies():Result<List<Movie>>
    suspend fun fetchPendingMovies():Result<List<Movie>>
    suspend fun downloadMovieToLocalFile(movie : String) : Result<String>

    //search
    suspend fun fetchMoviesBySearch(queryString: String): Result<List<Movie>>

}









