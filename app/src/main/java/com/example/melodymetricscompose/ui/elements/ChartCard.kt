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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melodymetricscompose.SongViewModel
import com.example.melodymetricscompose.db.Song
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
    //val chartEntryModelProducer = ChartEntryModelProducer(getRandomEntries())
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
                Chart(
                    chart = columnChart(context),
                    chartModelProducer = viewModel.chartEntryModelProducer,
                    startAxis = startAxis(),
                    bottomAxis = bottomAxis(),
                )

            }
        }
    }
}

//fun getRandomEntries() = List(4) { FloatEntry(it.toFloat(), (0f..16f).random()) }
fun getRandomEntries() = List(4) { FloatEntry(it.toFloat(), Random.nextFloat() * 16) }

// TODO move this function somewhere more appropriate
fun calculateAverageWeightedRating(songs: List<Song>): Float {
    var sumRatings = 0.0
    var countRatings = 0

    for (song in songs) {
        song.songRating?.let {
            sumRatings += it
            countRatings++
        }
    }

    return if (countRatings > 0) (sumRatings / countRatings).toFloat() else 0f
}


