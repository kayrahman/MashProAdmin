package com.nkr.mashproadmin.repo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nkr.bazaranocustomer.repo.local.dto.search.RoomSearchedWord
import com.nkr.mashproadmin.repo.local.model.MovieDTO


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