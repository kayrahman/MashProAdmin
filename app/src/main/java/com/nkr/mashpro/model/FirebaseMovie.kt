package com.nkr.mashpro.model

import android.os.Parcelable
import com.nkr.mashpro.base.BaseListAdapter
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

/*
data class FirebaseMovie(
    val video_url: String = "",
    val video_ref:String = "",
    val img_url: String = "",
    val movie_title: String = "",
    val movie_year: String = "",
    val description: String = "",
    val type : String =""

)*/

@Parcelize
data class Movie(
    var uid: String,
    val video_url: String,
    val video_ref:String?,
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
    val subscription_plan:String = "",
    val user_type : Int = 0,
    val registration_tokens: ArrayList<String> = arrayListOf<String>()
) : Parcelable


data class MovieLocationInfo(
    val video_url: String,
    val video_ref: String
)



data class Keyword(
    val product_uid: String = "",
    val keyword: String = "", override var listener: BaseListAdapter.AdapterListener?
) : BaseListAdapter.ListItemViewModel()



data class FirebaseKeyword(
    val product_uid: String = "",
    val keyword: String = ""
)


internal fun FirebaseKeyword.toKeyword() : Keyword{
    return Keyword(
        this.product_uid,
        this.keyword,
        null
    )
}