package com.example.melodymetricscompose.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.melodymetricscompose.SongViewModel
import com.example.melodymetricscompose.db.Song

/*
@Composable
fun SongListItem(song: Song) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = song.songContext,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = song.songRating.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}
*/
@Composable
fun SongListItem(song: Song) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = song.songContext,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = song.songRating.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun SongList(viewModel: SongViewModel){

    val songs by viewModel.songs.observeAsState()

    LazyColumn(modifier = Modifier.padding(vertical = 2.dp)) {
        itemsIndexed(songs ?: emptyList()) { _, song ->
            SongListItem(song)
        }
    }

}