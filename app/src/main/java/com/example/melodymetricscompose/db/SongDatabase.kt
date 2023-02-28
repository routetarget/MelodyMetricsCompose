package com.example.melodymetricscompose.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongDatabase : RoomDatabase(){

    abstract fun SongDao():SongDao

    // Singleton database
    companion object {
        @Volatile //Different threads modifying the same memory chunk
        private var INSTANCE: SongDatabase? = null // Nullable - only Instance or null
        fun getInstance(context: Context):SongDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song_database"
                    ).build()
                }
                return instance
            }
        }
    }
}