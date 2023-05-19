package com.example.melodymetricscompose.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.melodymetricscompose.util.Converters


@Database(entities = [Song::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SongDatabase : RoomDatabase(){

    abstract fun SongDao():SongDao

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null
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