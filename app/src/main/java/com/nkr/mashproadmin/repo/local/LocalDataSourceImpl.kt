package com.nkr.mashproadmin.repo.local

import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.bazaranocustomer.repo.remote.toMovies
import com.nkr.bazaranocustomer.repo.remote.toSearchWordListFromRoom
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.repo.Result
import com.nkr.mashproadmin.repo.local.model.MovieDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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



    //word
    override suspend fun insertSearchedWord(wordRoom: RoomSearchedWord): Result<Unit> {
        return withContext(Dispatchers.Default){
            try {
                dao.insertSearchWords(wordRoom)
               Result.Success(Unit)
            }catch (e:Exception){
               Result.Error(e)
            }
        }
    }

    override suspend fun clearAllSearchedWords(): Result<Unit> {
        return withContext(Dispatchers.Default){
            try {
                dao.clear()
                Result.Success(Unit)
            }catch (e:Exception){
               Result.Error(e)
            }
        }
    }

    override suspend fun getSearchedWords():Result<List<SearchedWord>> {
        return withContext(Dispatchers.Default){
            try {
                val words = dao.getAllSearchedWords().toSearchWordListFromRoom()
                Result.Success(words)
            }catch (e:Exception){
               Result.Error(e)
            }
        }
    }


}