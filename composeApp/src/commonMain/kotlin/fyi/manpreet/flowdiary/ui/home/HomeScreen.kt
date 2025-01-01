package fyi.manpreet.flowdiary.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fyi.manpreet.flowdiary.ui.home.components.appbar.HomeTopAppBar
import fyi.manpreet.flowdiary.ui.home.components.empty.HomeScreenEmpty
import fyi.manpreet.flowdiary.ui.theme.gradient

@Composable
fun HomeScreen(
    navController: NavController,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopAppBar() },
        floatingActionButton = {},
    ) { innerPadding ->
        if (true) {
            HomeScreenEmpty(
                modifier = Modifier.padding(innerPadding).background(brush = MaterialTheme.gradient.background)
            )
            return@Scaffold
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .background(brush = MaterialTheme.gradient.background)
        ) {

        }
    }
}