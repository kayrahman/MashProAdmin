package com.nkr.mashpro.repo

import android.net.Uri
import com.google.firebase.firestore.QuerySnapshot
import com.nkr.mashpro.model.FirebaseMovie
import com.nkr.mashpro.model.FirebaseUserInfo
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.model.MovieLocationInfo
import com.nkr.mashpro.repo.local.model.MovieDTO
import kotlinx.coroutines.flow.Flow

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//
    suspend fun setupUserInRemote(): Result<Unit>
    suspend fun getUserInfoFromRemote() : Result<FirebaseUserInfo>
    suspend fun updateUserSubscriptionPlanToRemote(sub_plan : String) : Result<Unit>

    //
    suspend fun uploadVideoInfoToRemote(uri:Uri) : Result<MovieLocationInfo>
    suspend fun uploadMovieThumbImageToRemote(uri:Uri) : Result<String>
    suspend fun uploadMovieInfoIntoRemote(movie:FirebaseMovie) : Result<Unit>

    //movie
    suspend fun fetchMoviesFromRemote():Result<List<Movie>>
    suspend fun downloadMovieFromRemote(movie : Movie) : Result<Unit>


    //------------LOCAL-----------------//
    suspend fun insertMoviesIntoLocalDb(movie: MovieDTO) : Result<Unit>
    suspend fun getDownloadedMoviesFromLocalDb() : Result<List<Movie>>


}