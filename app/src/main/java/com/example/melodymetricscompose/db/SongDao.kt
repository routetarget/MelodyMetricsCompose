package com.example.melodymetricscompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongDao {
    @Insert
    suspend fun insertSong(song: Song)

    // TODO other functions used -- vymyslet
    // TODO update function - Adding ratings?

    @Query("SELECT * FROM song_database")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM song_database WHERE song_id = (SELECT MAX(song_id) FROM song_database)")
    fun getLastPlayedSong(): LiveData<Song>
}