package com.nkr.mashproadmin.repo

import android.net.Uri
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.bazaranocustomer.repo.remote.toMovieDTO
import com.nkr.mashproadmin.model.FirebaseMovieInfo
import com.nkr.mashproadmin.model.FirebaseUserInfo
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.model.MovieLocationInfo
import com.nkr.mashproadmin.repo.local.ILocalDataSource
import com.nkr.mashproadmin.repo.local.model.MovieDTO
import com.nkr.mashproadmin.repo.remote.IRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MashProRepository(
    val remote: IRemoteDataSource,
    val local: ILocalDataSource,
) : IRepoDataSource {

    override suspend fun setupUserInRemote(): Result<Unit> {
        return remote.setupUserInRemote()
    }

    override suspend fun getUserInfoFromRemote(): Result<FirebaseUserInfo> {
        return remote.getUserInfo()
    }

    override suspend fun updateRemoteUserType(type: Int): Result<Unit> {
        return remote.updateUserType(type)
    }

    override suspend fun updateUserSubscriptionPlanToRemote(sub_plan: String): Result<Unit> {
        return remote.updateUserSubscriptionPlan(sub_plan)
    }

    override suspend fun uploadVideoInfoToRemote(uri: Uri): Result<MovieLocationInfo> {
        return remote.uploadVideoInfo(uri)
    }

    override suspend fun uploadMovieThumbImageToRemote(uri: Uri): Result<String> {
        return remote.uploadMovieThumbImage(uri)
    }

    override suspend fun uploadMovieInfoIntoRemote(movie: FirebaseMovieInfo): Result<Unit> {
        return remote.uploadMovieInfo(movie)
    }

    override suspend fun fetchNewMoviesFromRemote(): Result<List<Movie>> {
        return remote.fetchNewMovies()
    }
    override suspend fun fetchSlideMoviesFromRemote(): Result<List<Movie>> {
        return remote.fetchSlideMovies()
    }

    override suspend fun fetchOwnMoviesFromRemote(): Result<List<Movie>> {
        return remote.fetchOwnUploadedMovies()
    }


    override suspend fun downloadMovieFromRemote(movie: Movie): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {

                val download_response = movie.video_ref?.let { remote.downloadMovieToLocalFile(it) }
                when (download_response) {
                    is Result.Success -> {
                        val movie = movie.toMovieDTO
                        movie.copy(download_uri = download_response.data.toString())
                        //
                        val insert_response = insertMoviesIntoLocalDb(movie)
                        when (insert_response) {
                            is Result.Success -> {
                                Result.Success(Unit)
                            }
                            is Result.Error -> {
                                Result.Success(Unit)
                            }
                        }
                    }

                    is Result.Error -> {
                        Timber.i("video_download_uri : ${download_response.exception.toString()}")
                        throw Exception(download_response.exception.toString())

                    }
                    else -> {
                        throw Exception("Video not found")
                    }

                }


            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun fetchProductsBySearch(queryString: String): Result<List<Movie>> {
      return remote.fetchMoviesBySearch(queryString)
    }

    override suspend fun uploadUserThumbImageToRemote(uri: Uri): Result<String> {
        return remote.uploadUserThumbImage(uri)
    }

    override suspend fun updateRemoteImageRef(image_ref: String): Result<Unit> {
        return remote.updateImageRef(image_ref)
    }


    //-----------------LOCAL-----------------------//
    override suspend fun insertMoviesIntoLocalDb(movie: MovieDTO): Result<Unit> {
        return local.insertMovie(movie)
    }

    override suspend fun getDownloadedMoviesFromLocalDb(): Result<List<Movie>> {
        return local.getDownloadedMovie()
    }



    //----------------------------------SEARCH-----------------------------------//
    override suspend fun insertSearchedWord(wordRoom: RoomSearchedWord): Result<Unit> {
        return local.insertSearchedWord(wordRoom)
    }

    override suspend fun clearAllSearchedWords(): Result<Unit> {
        return local.clearAllSearchedWords()
    }

    override suspend fun getSearchedWords(): Result<List<SearchedWord>> {
        return local.getSearchedWords()
    }




}

