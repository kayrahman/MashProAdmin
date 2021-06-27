package com.nkr.mashpro.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nkr.mashpro.repo.local.model.MovieDTO


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie : MovieDTO)

    @Query("SELECT * FROM movie_info")
    fun getAllDownloadedMovies(): List<MovieDTO>

}