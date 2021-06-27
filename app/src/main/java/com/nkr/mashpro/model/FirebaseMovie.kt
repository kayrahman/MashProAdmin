package com.nkr.mashpro.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList


data class FirebaseMovie(
    val video_url: String = "",
    val video_ref:String = "",
    val img_url: String = "",
    val movie_title: String = "",
    val movie_year: String = "",
    val description: String = "",
    val type : String =""

)

@Parcelize
data class Movie(
    var uid: String,
    val video_url: String,
    val img_url: String,
    val movie_title: String,
    val movie_year: String,
    val description: String,
    val type : String
):Parcelable


@Parcelize
data class FirebaseUserInfo(
    val uid: String = "",
    val user_name: String = "",
    val email: String = "",
    val img_url: String = "",
    val registration_tokens: ArrayList<String> = arrayListOf<String>()
) : Parcelable


data class MovieLocationInfo(
    val video_url: String,
    val video_ref: String
)