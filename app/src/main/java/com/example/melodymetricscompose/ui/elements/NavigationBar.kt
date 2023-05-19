package com.example.melodymetricscompose.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.melodymetricscompose.SongViewModel

@Composable
fun Navigation(navController: NavHostController, viewModel: SongViewModel){
    NavHost(navController = navController, startDestination = "mainScreen"){
        composable("mainScreen"){
            Home(name = "home screen", viewModel = viewModel)
        }
        composable("history"){
            HistoryScreen(name = "History", viewModel = viewModel)
        }
        composable("settings"){
            SettingsScreen(name = "SettingsScreen")
        }

    }
}

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
){
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.primaryContainer) {
        items.forEach{item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(selected = item.route == navController.currentDestination?.route, onClick = {onItemClick(item)}, icon = {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {


                    Icon(imageVector = item.icon, contentDescription = item.name)
                    if (selected) {

                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                }
            })



        }
    }
}

@Composable
fun Home(name: String, modifier: Modifier = Modifier, viewModel: SongViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                .padding(top = 16.dp)
        ) {
            CurrentlyPlayingCard(viewModel)
        }
        Spacer(modifier = Modifier.height(16.dp))
        StatsCard(viewModel,40)
        Spacer(modifier = Modifier.height(16.dp))
        StatsCard(viewModel,1)
        Spacer(modifier = Modifier.height(16.dp))
        //BarChartCardDynamic(viewModel = viewModel)
    }
}

@Composable
fun HistoryScreen(name: String, modifier: Modifier = Modifier, viewModel: SongViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                .padding(top = 16.dp)
        ) {
        }
        SongList(viewModel = viewModel)
    }
}


@Composable
fun SettingsScreen(name: String, modifier: Modifier = Modifier) {
    // Create state holders
    val checkbox1State = remember { mutableStateOf(false) }
    val checkbox2State = remember { mutableStateOf(false) }
    val sliderState = remember { mutableStateOf(2f) } // slider range from 2 to 50

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Nastavení",
            style = typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checkbox1State.value,
                onCheckedChange = { checkbox1State.value = it }
            )
            Text(text = "RYM", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checkbox2State.value,
                onCheckedChange = { checkbox2State.value = it }
            )
            Text(text = "Metacritic", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Historie dní: ${sliderState.value.toInt()}")

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = sliderState.value,
            onValueChange = { sliderState.value = it },
            valueRange = 2f..50f,
        )
    }
}
