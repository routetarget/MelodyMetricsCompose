package com.example.melodymetricscompose.ui.elements

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.melodymetricscompose.SongViewModel
import kotlin.math.round

@SuppressLint("RememberReturnType")
@Composable
fun StatsCard(viewModel: SongViewModel, numberOfDays: Int) {
    val averageRating = remember { mutableStateOf<Double?>(null) }
    LaunchedEffect(key1 = Unit) {
        val rawAverageRating = viewModel.getAverageRatingLastNDays(numberOfDays)
        averageRating.value = round(rawAverageRating!! * 100) / 100
        Log.d("Stats","Average rating over last days is: $averageRating")
    }

    DispCard(averageRating, numberOfDays)
}
@Composable
fun DispCard(averageRating: MutableState<Double?>, numberOfDays: Int) {
    Surface(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.3f),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            Column(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (numberOfDays == 1) "Průměrné hodnocení dnes" else "Průměrné hodnocení za posledních $numberOfDays dní:",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                    //Text(text = averageRating.toString(), style = MaterialTheme.typography.headlineSmall)
                }
            }
            mutableRatingCard(overallRating = averageRating.value, modifier = androidx.compose.ui.Modifier.padding(8.dp))
        }
    }
}

@Composable
fun mutableRatingCard(overallRating: Double?, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxHeight(0.5f)
            .width(100.dp),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = overallRating.toString(),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}