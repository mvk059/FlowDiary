package fyi.manpreet.flowdiary

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fyi.manpreet.flowdiary.navigation.HomeDestination
import fyi.manpreet.flowdiary.ui.home.HomeScreen
import fyi.manpreet.flowdiary.ui.home.HomeViewModel
import fyi.manpreet.flowdiary.ui.theme.FlowTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    FlowTheme {

        NavHost(
            navController = navController,
            startDestination = HomeDestination,
        ) {

            composable<HomeDestination> {
                HomeScreen(
                    navController = navController,
                )
            }
        }
    }
}