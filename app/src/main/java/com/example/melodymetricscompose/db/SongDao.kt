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

    //@Query("SELECT * FROM song_database WHERE song_id = (SELECT MAX(song_id) FROM song_database)")
    @Query("SELECT * FROM song_database ORDER BY song_id DESC LIMIT 1")
    fun getLastPlayedSong(): LiveData<Song>

    // Select data for charting
    /*
    @Query("SELECT * FROM song_database WHERE strftime('%s', 'now') - strftime('%s', song_context) <= :days * 86400")
    fun getSongsInLastNDays(days: Int): LiveData<List<Song>>
*/

    @Query("SELECT * FROM song_database WHERE julianday('now') - julianday(date_created) <= :days")
    fun getSongsInLastNDays(days: Int): LiveData<List<Song>>

}
