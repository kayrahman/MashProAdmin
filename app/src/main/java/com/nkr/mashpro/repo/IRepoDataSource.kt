package com.nkr.mashpro.repo

import android.net.Uri
import com.google.firebase.firestore.QuerySnapshot
import com.nkr.mashpro.model.FirebaseMovie
import kotlinx.coroutines.flow.Flow

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//


    //------------------ENDING NOTIFICAITON------------------//

    suspend fun setupUserInRemote(): Result<Unit>
    suspend fun uploadVideoInfoToRemote(uri:Uri) : Result<String>
    suspend fun uploadMovieThumbImageToRemote(uri:Uri) : Result<String>
    suspend fun uploadMovieInfoIntoRemote(movie:FirebaseMovie) : Result<Unit>

}