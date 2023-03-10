package com.example.melodymetricscompose.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = "home"){
        composable("mainScreen"){
            home(name = "home screen")
        }
        composable("history"){
            ChartScreen(name = "Chart Chart")
        }
        composable("settings"){
            SettingsScreen(name = "SettingsScreen")
        }



    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
){
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(modifier = modifier, containerColor = Color.DarkGray) {
        items.forEach{item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(selected = item.route == navController.currentDestination?.route, onClick = {onItemClick(item)}, icon = {
                Icon(imageVector = item.icon, contentDescription = item.name)
                if(selected){
                    Text(
                        text = item.name,
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp
                    )
                }
            })



        }
    }
}

@Composable
fun home(name: String, modifier: Modifier = Modifier) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                .padding(top = 16.dp)
        ) {
            CurrentlyPlayingCard(viewModel)
        }
    }
}

@Composable
fun ChartScreen(name: String, modifier: Modifier = Modifier) {
    BarChartCard()
    BarChartCardDynamic()
}


@Composable
fun SettingsScreen(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
