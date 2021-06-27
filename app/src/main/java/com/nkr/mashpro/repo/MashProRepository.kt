package com.nkr.mashpro.repo

import android.net.Uri
import com.nkr.mashpro.model.FirebaseMovie
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.model.MovieLocationInfo
import com.nkr.mashpro.repo.local.ILocalDataSource
import com.nkr.mashpro.repo.local.model.MovieDTO
import com.nkr.mashpro.repo.remote.IRemoteDataSource
import timber.log.Timber

class MashProRepository(val remote: IRemoteDataSource,
                        val local: ILocalDataSource,
                        ) : IRepoDataSource {

    override suspend fun setupUserInRemote(): Result<Unit> {
        return remote.setupUserInRemote()
    }

    override suspend fun uploadVideoInfoToRemote(uri: Uri): Result<MovieLocationInfo> {
        return remote.uploadVideoInfo(uri)
    }

    override suspend fun uploadMovieThumbImageToRemote(uri: Uri): Result<String> {
        return remote.uploadMovieThumbImage(uri)
    }

    override suspend fun uploadMovieInfoIntoRemote(movie : FirebaseMovie): Result<Unit> {
        return remote.uploadMovieInfo(movie)
    }

    override suspend fun fetchMoviesFromRemote(): Result<List<Movie>> {
        return remote.fetchMovies()
    }

    override suspend fun downloadMovieFromRemote(downloadUrl: String): Result<Unit> {
        val download_response = remote.downloadMovieToLocalFile(downloadUrl)
        when(download_response){
            is Result.Success -> {
                Timber.i("video_download_uri : ${download_response.data.toString()}")
            }
            is Result.Error ->{
                Timber.i("video_download_uri : ${download_response.exception.toString()}")
            }
        }

       return Result.Success(Unit)
    }

    //-----------------LOCAL-----------------------//
    override suspend fun insertMoviesIntoLocalDb(movie: MovieDTO) : Result<Unit> {
      return local.insertMovie(movie)
    }

    override suspend fun getDownloadedMoviesFromLocalDb(): Result<List<Movie>> {
        return local.getDownloadedMovie()
    }


}