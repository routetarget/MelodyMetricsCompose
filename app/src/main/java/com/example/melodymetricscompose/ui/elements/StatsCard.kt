package com.example.melodymetricscompose.ui.elements

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.melodymetricscompose.SongViewModel
import com.example.melodymetricscompose.db.Song
import java.lang.reflect.Modifier

@SuppressLint("RememberReturnType")
@Composable
fun StatsCard(viewModel: SongViewModel) {
    val averageRating = remember { mutableStateOf<Double?>(null) }
    LaunchedEffect(key1 = Unit) {
        averageRating.value = viewModel.getAverageRatingLastNDays(7)
    }

    Card(averageRating)
}

@Composable
fun Card(averageRating: MutableState<Double?>) {
    Surface(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.3f),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            Text(text = "Prumerny rating za posledni tyden:", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            Text(text = averageRating.toString(), style = MaterialTheme.typography.headlineSmall)
        }
    }
}
