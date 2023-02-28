package com.example.melodymetricscompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melodymetricscompose.db.Song
import com.example.melodymetricscompose.db.SongDao
import kotlinx.coroutines.launch

class SongViewModel(private val dao: SongDao): ViewModel() {

    val songs = dao.getAllSongs()
    val lastPlayedSong = dao.getLastPlayedSong()

    fun insertSong(song: Song)=viewModelScope.launch {
        dao.insertSong(song = song)
    }



}