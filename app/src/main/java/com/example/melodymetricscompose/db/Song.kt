package com.example.melodymetricscompose.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_database")
data class Song(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    var id:Long,
    @ColumnInfo(name = "song_title")
    var title:String,
    @ColumnInfo(name = "song_context")
    var songContext:String,
    @ColumnInfo(name = "song_rating")
    var songRating: Double?,
    @ColumnInfo(name = "song_rym_link")
    var rymLink: String?
)

