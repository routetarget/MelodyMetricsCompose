package com.example.melodymetricscompose

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.melodymetricscompose.db.Song
import com.example.melodymetricscompose.db.SongDatabase
import com.example.melodymetricscompose.mediareaders.MediaBroadcastReceiver
import com.example.melodymetricscompose.mediareaders.TrackChangedEvent
import com.example.melodymetricscompose.scrapers.AOYRatingFetcher
import com.example.melodymetricscompose.scrapers.RYMRatingFetcher
import com.example.melodymetricscompose.ui.elements.BottomNavBar
import com.example.melodymetricscompose.ui.elements.BottomNavItem
import com.example.melodymetricscompose.ui.elements.Navigation
import com.example.melodymetricscompose.ui.theme.MelodyMetricsComposeTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.time.LocalDate
import androidx.datastore.preferences.preferencesDataStore

class MainActivity : ComponentActivity() {

    private val mediaReceiver = MediaBroadcastReceiver()
    private lateinit var viewModel: SongViewModel
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



            val dao = SongDatabase.getInstance(application).SongDao()
            val factory = SongViewModelFactory(dao, applicationContext.dataStore)
            viewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

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
                            .padding(top = 2.dp, start = 4.dp, end = 4.dp, bottom = 2.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        //viewModel.fetchLastNDaysSongData(dao.getSongsInLastNDays(5))
                        Scaffold(
                            bottomBar = {
                                BottomNavBar(items = listOf(
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
                                    onItemClick = { navController.navigate(it.route) })
                            }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTrackChanged(event: TrackChangedEvent) {
        val timestamp: Long = System.currentTimeMillis()
        val currentDate = LocalDate.now()
        lifecycleScope.launch{
            val defferedScrapedInfoRYM = async { RYMRatingFetcher.fetchRating(event.artist, event.album)}
            //val defferedScrapedInfoAOY = async { AOYRatingFetcher.fetchRating(event.artist, event.album)}

            val scrapedInfoRYM = defferedScrapedInfoRYM.await()

            if (scrapedInfoRYM.rating != null){
                viewModel.insertSong(Song(timestamp, event.title, event.album, scrapedInfoRYM.rating, scrapedInfoRYM.url, currentDate)) //TODO pass RYM link
                Log.d("FETCHED","Albums ${event.album} rating is: $scrapedInfoRYM.rating")
            } else {
                viewModel.insertSong(Song(timestamp, event.title, event.album, null, null, currentDate))
                Log.d("FETCHED","Album rating NOT FETCHED")
            }

        }
    }
    companion object {
        private val MIN_SUPPORTED_VERSION = Build.VERSION_CODES.Q // Minimum supported version (Android 5.0 - API level 21)
    }

}
