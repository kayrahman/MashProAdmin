package com.nkr.mashpro.repo

import android.net.Uri
import com.nkr.bazaranocustomer.repo.remote.toMovieDTO
import com.nkr.mashpro.model.FirebaseMovieInfo
import com.nkr.mashpro.model.FirebaseUserInfo
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.model.MovieLocationInfo
import com.nkr.mashpro.repo.local.ILocalDataSource
import com.nkr.mashpro.repo.local.model.MovieDTO
import com.nkr.mashpro.repo.remote.IRemoteDataSource
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

    override suspend fun fetchMoviesFromRemote(): Result<List<Movie>> {
        return remote.fetchMovies()
    }

    override suspend fun downloadMovieFromRemote(movie: Movie): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val download_response = remote.downloadMovieToLocalFile(movie.video_url)
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
}
