package com.abdulaziz.a4kfullwallpapers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdulaziz.a4kfullwallpapers.models.ImageModel

@Database(entities = [ImageModel::class], version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun imageDao(): ImageDao

    companion object {
        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                    "my_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return db!!
        }
    }

}