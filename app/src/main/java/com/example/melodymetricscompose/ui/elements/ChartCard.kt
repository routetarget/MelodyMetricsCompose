package com.example.melodymetricscompose.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melodymetricscompose.SongViewModel
import com.example.melodymetricscompose.db.Song
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.model.BarData
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.views.chart.column.columnChart
import kotlin.random.Random

@Composable
fun BarChartCardDynamic(viewModel: SongViewModel) {
    val dailyAverageRatingsLast14Days by viewModel.dailyAverageRatingsLast14Days.observeAsState(emptyList())
    androidx.compose.material3.Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Rating history",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val context = LocalContext.current
                BarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    onBarClick = {/* TODO */},
                    color = MaterialTheme.colorScheme.primaryContainer,
                    barData = dailyAverageRatingsLast14Days
                )

            }
        }
    }
}

//fun getRandomEntries() = List(4) { FloatEntry(it.toFloat(), (0f..16f).random()) }
fun getRandomEntries() = List(4) { FloatEntry(it.toFloat(), Random.nextFloat() * 16) }

// TODO move this function somewhere more appropriate
fun calculateAverageWeightedRating(songs: List<Song>): Float {
    var sumRatings = 6.0//0.0
    var countRatings = 2.0 //0

    songs.forEach { song ->
        song.songRating?.let {
            sumRatings += it
            countRatings++
        }
    }

    return if (countRatings > 0) (sumRatings / countRatings).toFloat() else 0f
}


