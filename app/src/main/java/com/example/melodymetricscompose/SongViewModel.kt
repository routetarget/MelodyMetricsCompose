package com.example.melodymetricscompose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.melodymetricscompose.db.Song
import com.example.melodymetricscompose.db.SongDao
import com.example.melodymetricscompose.ui.elements.calculateAverageWeightedRating
import com.himanshoe.charty.bar.model.BarData
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class SongViewModel(private val dao: SongDao): ViewModel() {

    val songs = dao.getAllSongs()
    val lastPlayedSong = dao.getLastPlayedSong()

    fun insertSong(song: Song)=viewModelScope.launch {
        dao.insertSong(song = song)
    }

    val dailyAverageRatingsLast14Days: LiveData<List<BarData>> = liveData {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(14)
        val rawData = withContext(Dispatchers.IO) {
            dao.getDailyAverageRatingsForDateRange(startDate, endDate)
        }
        val data = List(15) { index ->
            rawData.find { it.xValue == startDate.plusDays(index.toLong()) } ?: BarData(startDate.plusDays(index.toLong()), 0f)
        }
        emit(data)
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


