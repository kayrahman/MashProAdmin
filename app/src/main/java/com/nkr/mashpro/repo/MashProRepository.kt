package com.nkr.mashpro.repo

import android.net.Uri
import com.nkr.mashpro.model.FirebaseMovie
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.remote.IRemoteDataSource

class MashProRepository(val remote: IRemoteDataSource) : IRepoDataSource {

    override suspend fun setupUserInRemote(): Result<Unit> {
        return remote.setupUserInRemote()
    }

    override suspend fun uploadVideoInfoToRemote(uri: Uri): Result<String> {
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
}