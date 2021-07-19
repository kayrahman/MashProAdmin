package com.nkr.mashpro.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.mashpro.repo.local.model.MovieDTO


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie : MovieDTO)

    @Query("SELECT * FROM movie_info")
    fun getAllDownloadedMovies(): List<MovieDTO>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchWords(roomSearched_word: RoomSearchedWord)

    @Query("DELETE FROM searched_words_table")
    fun clear()

    @Query("SELECT * FROM searched_words_table ORDER BY searchId DESC")
    suspend fun getAllSearchedWords(): List<RoomSearchedWord>


}