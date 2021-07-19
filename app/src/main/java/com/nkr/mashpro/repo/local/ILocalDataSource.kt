package com.nkr.mashpro.repo.local


import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.local.model.MovieDTO
import kotlinx.coroutines.flow.Flow
import com.nkr.mashpro.repo.Result


interface ILocalDataSource {
    //--------------------MOVIE-----------------------//
    suspend fun insertMovie(movie : MovieDTO): Result<Unit>
    suspend fun getDownloadedMovie(): Result<List<Movie>>

    //
    suspend fun insertSearchedWord(wordRoom: RoomSearchedWord): Result<Unit>
    suspend fun clearAllSearchedWords(): Result<Unit>
    suspend fun getSearchedWords(): Result<List<SearchedWord>>

}