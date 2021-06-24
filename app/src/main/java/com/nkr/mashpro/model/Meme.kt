package com.nkr.mashpro.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FreshMeme(
    val uid: String,
    val img_url: String,
    val post_title: String,
    val upvote_count: Int,
    val downvote_count: Int,
    val comment_count: Int,
    val category_name: String,
    val section_uid: String,
    val sectionTitle: String,
    var isUpvoted: Boolean,
    var isDownVoted: Boolean
) : Parcelable