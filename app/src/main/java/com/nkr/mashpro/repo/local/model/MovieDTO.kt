package com.nkr.mashpro.repo.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movie_info")
data class MovieDTO(
    @PrimaryKey
    val uid: String,
    val video_url: String,
    val video_ref:String,
    val img_url: String,
    val movie_title: String,
    val movie_year: String,
    val description: String,
    val type : String
)