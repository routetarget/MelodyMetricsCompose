package com.example.melodymetricscompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.melodymetricscompose.db.Song
import com.example.melodymetricscompose.db.SongDao
import com.example.melodymetricscompose.ui.elements.calculateAverageWeightedRating
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class SongViewModel(private val dao: SongDao): ViewModel() {

    val songs = dao.getAllSongs()
    val lastPlayedSong = dao.getLastPlayedSong()

    fun insertSong(song: Song)=viewModelScope.launch {
        dao.insertSong(song = song)
    }


    // Chart
    private var _chartEntries = MutableLiveData<List<ChartEntry>>()
    val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(_chartEntries.value ?: emptyList())


    private fun getChartEntries(songs: List<Song>, nDays: Int): List<ChartEntry> {
        val songsByDay = songs.groupBy { Instant.ofEpochMilli(it.id).atZone(ZoneId.systemDefault()).toLocalDate()}
        return (0 until nDays).map { dayIndex ->
            val currentDate = LocalDate.now().minusDays(dayIndex.toLong())
            val songsForDay = songsByDay[currentDate] ?: emptyList()
            val averageWeightedRating = calculateAverageWeightedRating(songsForDay)
            FloatEntry(x = dayIndex.toFloat(), y = averageWeightedRating)

        }
    }

    fun fetchLastFiveDaysData(songsLiveData: LiveData<List<Song>>) {
        _chartEntries = songsLiveData.map { songs ->
            getChartEntries(songs, 5)
        } as MutableLiveData<List<ChartEntry>>
    }


/*
    fun updateChartEntries(songs: List<Song>) {
        val averageWeightedRating = calculateAverageWeightedRating(songs)
        val entry = FloatEntry(x = 0f, y = averageWeightedRating)
        _chartEntries.value = listOf(entry)
    }
*/



}


