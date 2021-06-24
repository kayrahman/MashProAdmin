package com.nkr.mashpro.repo

import android.net.Uri
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//


    //------------------ENDING NOTIFICAITON------------------//

    suspend fun setupUserInRemote(): Result<Unit>
    suspend fun uploadVideoInfoToRemote(uri:Uri) : Result<String>

}