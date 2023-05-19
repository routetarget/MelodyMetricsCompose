package com.example.melodymetricscompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.himanshoe.charty.bar.model.BarData
import java.time.LocalDate

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


    @Query("SELECT AVG(song_rating) FROM song_database WHERE julianday('now') - julianday(date_created) <= :days")
    suspend fun getAverageRatingLastNDays(days: Int): Double?

    @Query("""
    SELECT date_created as xValue, AVG(song_rating) as yValue
    FROM song_database
    WHERE date_created >= :startDate AND date_created <= :endDate
    GROUP BY date_created
    ORDER BY date_created
""")
    suspend fun getDailyAverageRatingsForDateRange(startDate: LocalDate, endDate: LocalDate): List<BarData>



}
