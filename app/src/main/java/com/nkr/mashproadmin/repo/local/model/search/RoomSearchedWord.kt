package com.nkr.bazaranocustomer.repo.local.dto.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nkr.mashproadmin.base.BaseListAdapter


data class SearchedWord(val searched_word:String,
                        override var listener: BaseListAdapter.AdapterListener?
) : BaseListAdapter.ListItemViewModel()

@Entity(tableName = "searched_words_table", indices = [Index(value = ["searched_word"], unique = true)] )
data class RoomSearchedWord(
    @PrimaryKey(autoGenerate = true)
    var searchId: Long = 0L,

    @ColumnInfo(name = "searched_word")
    val searched_word:String = ""
)
