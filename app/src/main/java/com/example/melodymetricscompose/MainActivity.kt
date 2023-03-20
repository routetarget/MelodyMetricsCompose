package com.example.melodymetricscompose

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.melodymetricscompose.db.Song
import com.example.melodymetricscompose.db.SongDatabase
import com.example.melodymetricscompose.mediareaders.MediaBroadcastReceiver
import com.example.melodymetricscompose.mediareaders.TrackChangedEvent
import com.example.melodymetricscompose.scrapers.RYMRatingFetcher
import com.example.melodymetricscompose.ui.elements.BottomNavBar
import com.example.melodymetricscompose.ui.elements.BottomNavItem
import com.example.melodymetricscompose.ui.elements.Card
import com.example.melodymetricscompose.ui.elements.SongList
import com.example.melodymetricscompose.ui.elements.CurrentlyPlayingCard
import com.example.melodymetricscompose.ui.elements.Navigation
import com.example.melodymetricscompose.ui.theme.MelodyMetricsComposeTheme
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : ComponentActivity() {

    private val mediaReceiver = MediaBroadcastReceiver()
    private lateinit var viewModel: SongViewModel


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dao = SongDatabase.getInstance(application).SongDao()
        val factory = SongViewModelFactory(dao)
        viewModel = ViewModelProvider(this,factory).get(SongViewModel::class.java)
        //val lastPlayedSong: Song? by viewModel.lastPlayedSong.observeAsState(initial = null)

        // TODO predelat do peknych objectku at to neni takto shit
        // TODO Dalsi intenty pro YT Music a Amazon music
        val DefaultFilter = IntentFilter("com.android.music.metachanged")
        val SpotifyFilter = IntentFilter("com.spotify.music.metadatachanged")


        registerReceiver(mediaReceiver, DefaultFilter)
        registerReceiver(mediaReceiver, SpotifyFilter)

        // Subscribe to TrackChangedEvent on the event Bus
        EventBus.getDefault().register(this)

        setContent {
            MelodyMetricsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp), //TODO paddings a spacers -> Detailne prostudovat, je to ugly rn
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomNavBar(items = listOf(
                                BottomNavItem(
                                    name = "Home",
                                    route = "mainScreen",
                                    icon = Icons.Default.Home
                                ),
                                BottomNavItem(
                                    name = "History",
                                    route = "history",
                                    icon = Icons.Default.List
                                ),
                                BottomNavItem(
                                    name = "Settings",
                                    route = "settings",
                                    icon = Icons.Default.Settings
                                ),
                            ), navController = navController, 
                            onItemClick = {navController.navigate(it.route)})}
                    ) {
                        Navigation(navController = navController, viewModel = viewModel)
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unregisterReceiver(mediaReceiver)

    }

// TODO track nekolikrat za sebou
    // TODO Every time main activity is restarted, scraper runs again -- check that
    // TODO app is fetched every time on start
    // TODO nekolik tracku za sebou
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTrackChanged(event: TrackChangedEvent) {
        val timestamp: Long = System.currentTimeMillis() // Timestamp je Primary Key -> stejny song, jiny cas == 2 database entries
        Log.d("BROADCAST","ALBUM: ${event.album}, TITLE: ${event.title}")
        lifecycleScope.launch{
            val scrapedInfo = RYMRatingFetcher.fetchRating(event.artist, event.album)
            if (scrapedInfo.rating != null){
                viewModel.insertSong(Song(timestamp, event.title, event.album, scrapedInfo.rating, scrapedInfo.url)) //TODO pass RYM link
                Log.d("FETCHED","Albums ${event.album} rating is: $scrapedInfo.rating")
            } else {
                viewModel.insertSong(Song(timestamp, event.title, event.album, null, null))
                Log.d("FETCHED","Album rating NOT FETCHED")
            }

        }
    }

}
