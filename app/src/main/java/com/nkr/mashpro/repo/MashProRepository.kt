package com.nkr.mashpro.repo

import android.net.Uri
import com.nkr.mashpro.repo.remote.IRemoteDataSource

class MashProRepository(val remote: IRemoteDataSource) : IRepoDataSource {

    override suspend fun setupUserInRemote(): Result<Unit> {
        return remote.setupUserInRemote()
    }

    override suspend fun uploadVideoInfoToRemote(uri: Uri): Result<String> {
        return remote.uploadVideoInfo(uri)
    }
}