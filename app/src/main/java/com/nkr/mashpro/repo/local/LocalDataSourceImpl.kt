package com.nkr.mashpro.repo.local

import com.nkr.bazaranocustomer.repo.remote.toMovies
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.Result
import com.nkr.mashpro.repo.local.model.MovieDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocalDataSourceImpl(val dao: MovieDao) : ILocalDataSource {

    override suspend fun insertMovie(movie: MovieDTO): Result<Unit> {
        return withContext(Dispatchers.Default) {
            try {
                dao.insertMovie(movie)
                Result.Success(Unit)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun clearAllSearchedWords(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getDownloadedMovie(): Result<List<Movie>> {
        return withContext(Dispatchers.Default) {
            try {
               val movies = dao.getAllDownloadedMovies()
                Result.Success(movies.toMovies)
            } catch (e: Exception) {
                throw e
            }
        }
    }

}