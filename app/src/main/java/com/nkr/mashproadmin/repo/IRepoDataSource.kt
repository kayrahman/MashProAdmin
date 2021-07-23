package com.nkr.mashproadmin.repo

import android.net.Uri
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashproadmin.model.FirebaseMovieInfo
import com.nkr.mashproadmin.model.FirebaseUserInfo
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.model.MovieLocationInfo
import com.nkr.mashproadmin.repo.local.model.MovieDTO

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//
    suspend fun setupUserInRemote(): Result<Unit>
    suspend fun getUserInfoFromRemote() : Result<FirebaseUserInfo>
    suspend fun updateRemoteUserType(type:Int) : Result<Unit>
    suspend fun updateUserSubscriptionPlanToRemote(sub_plan : String) : Result<Unit>

    //
    suspend fun uploadVideoInfoToRemote(uri:Uri) : Result<MovieLocationInfo>
    suspend fun uploadMovieThumbImageToRemote(uri:Uri) : Result<String>
    suspend fun uploadMovieInfoIntoRemote(movie:FirebaseMovieInfo) : Result<Unit>

    //movie
    suspend fun fetchNewMoviesFromRemote():Result<List<Movie>>
    suspend fun fetchSlideMoviesFromRemote():Result<List<Movie>>
    suspend fun fetchOwnMoviesFromRemote():Result<List<Movie>>

    suspend fun fetchPendingMoviesFromRemote():Result<List<Movie>>
    suspend fun downloadMovieFromRemote(movie : Movie) : Result<Unit>

    //search
    //-------------------------------search--------------------//
    suspend fun fetchProductsBySearch(queryString: String): Result<List<Movie>>

    //storage
    suspend fun uploadUserThumbImageToRemote(uri:Uri) : Result<String>
    suspend fun updateRemoteImageRef(image_ref : String) : Result<Unit>


    //------------LOCAL-----------------//
    suspend fun insertMoviesIntoLocalDb(movie: MovieDTO) : Result<Unit>
    suspend fun getDownloadedMoviesFromLocalDb() : Result<List<Movie>>

//

    suspend fun insertSearchedWord(wordRoom: RoomSearchedWord): Result<Unit>
    suspend fun clearAllSearchedWords():Result<Unit>
    suspend fun getSearchedWords(): Result<List<SearchedWord>>

}