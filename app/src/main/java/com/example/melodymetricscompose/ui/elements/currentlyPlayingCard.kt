package com.example.melodymetricscompose.ui.elements

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.melodymetricscompose.SongViewModel
import com.example.melodymetricscompose.db.Song

@Composable
fun currentlyPlayingCard(viewModel: SongViewModel){

    val lastPlayedSongsList by viewModel.songs.observeAsState()
    val lastPlayedSong: Song? = lastPlayedSongsList?.firstOrNull()

    if (lastPlayedSong != null) {
        Card(song = lastPlayedSong)
    }


}


@Composable
fun Card(song: Song) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.3f),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = song.songContext, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = song.title, style = MaterialTheme.typography.headlineSmall)
            //ClickableChip(text = "Click Me!", onClick = {})
            song.rymLink?.let { CardButton(url = it) }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CardButton(url: String) {
    val context = LocalContext.current
    Button(
        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) },
        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("RYM")
    }
}

@Composable
fun ClickableChip(text: String, onClick: () -> Unit) {
    Chip(
        modifier = Modifier.clickable(onClick = onClick),
        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colorScheme.primary,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        text = { Text(text) }
    )
}


@Composable
fun Chip(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    border: BorderStroke? = null,
    text: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(50),
        border = border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            text()
        }
    }
}