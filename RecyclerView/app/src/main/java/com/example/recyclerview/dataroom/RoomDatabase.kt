package com.example.recyclerview.dataroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recyclerview.DATABASE_NAME
import com.example.recyclerview.movie.Movie

@Database(entities = [Movie::class], version = 3)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO

    companion object {
        @Volatile
        private var instance: com.example.recyclerview.dataroom.RoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance  = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            com.example.recyclerview.dataroom.RoomDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries() //don't use this line in product. it just for demo
            .build()
    }
}