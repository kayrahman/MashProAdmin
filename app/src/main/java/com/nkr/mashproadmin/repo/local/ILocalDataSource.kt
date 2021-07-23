package com.nkr.mashproadmin.repo.local


import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashproadmin.model.Movie
import com.nkr.mashproadmin.repo.local.model.MovieDTO
import com.nkr.mashproadmin.repo.Result


interface ILocalDataSource {
    //--------------------MOVIE-----------------------//
    suspend fun insertMovie(movie : MovieDTO): Result<Unit>
    suspend fun getDownloadedMovie(): Result<List<Movie>>

    //
    suspend fun insertSearchedWord(wordRoom: RoomSearchedWord): Result<Unit>
    suspend fun clearAllSearchedWords(): Result<Unit>
    suspend fun getSearchedWords(): Result<List<SearchedWord>>

}