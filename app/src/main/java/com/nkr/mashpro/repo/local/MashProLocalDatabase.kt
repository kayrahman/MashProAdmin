package com.nkr.mashpro.repo.local

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.bazaranocustomer.repo.local.dto.search.SearchedWord
import com.nkr.mashpro.model.Movie
import com.nkr.mashpro.repo.local.model.MovieDTO


@Database(entities = [MovieDTO::class, RoomSearchedWord::class], version = 1, exportSchema = false)
abstract class MashProLocalDatabase: RoomDatabase() {
    abstract val movieDao : MovieDao
    companion object {

        @Volatile
        private var INSTANCE: MashProLocalDatabase? = null

        fun getInstance(context: Context): MashProLocalDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            MashProLocalDatabase::class.java,
                            "mashpro_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

}