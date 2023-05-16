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
import kotlinx.coroutines.launch
import java.time.LocalDate

class SongViewModel(private val dao: SongDao): ViewModel() {

    val songs = dao.getAllSongs()
    val lastPlayedSong = dao.getLastPlayedSong()

    fun insertSong(song: Song)=viewModelScope.launch {
        dao.insertSong(song = song)
    }


    // Chart
    private var _chartEntries = MutableLiveData<List<ChartEntry>>()
    val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(_chartEntries.value ?: emptyList())


    // Retreive
    private fun getChartEntries(songs: List<Song>, nDays: Int): List<ChartEntry> {
        val songsByDay = songs.groupBy {it.dateCreated}
        return (0 until nDays).map { dayIndex ->
            val currentDate = LocalDate.now().minusDays(dayIndex.toLong())
            val songsForDay = songsByDay[currentDate] ?: emptyList()
            val averageWeightedRating = calculateAverageWeightedRating(songsForDay)
            FloatEntry(x = dayIndex.toFloat(), y = averageWeightedRating)

        }
    }

    fun fetchLastNDaysSongData(songsLiveData: LiveData<List<Song>>) {
        _chartEntries = songsLiveData.map { songs ->
            getChartEntries(songs, 5)
        } as MutableLiveData<List<ChartEntry>>
    }

    suspend fun getAverageRatingLastNDays(days: Int): Double? {
        return dao.getAverageRatingLastNDays(days)
    }

}


