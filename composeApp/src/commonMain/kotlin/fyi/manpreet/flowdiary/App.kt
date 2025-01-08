package fyi.manpreet.flowdiary

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fyi.manpreet.flowdiary.data.model.AudioPath
import fyi.manpreet.flowdiary.navigation.HomeDestination
import fyi.manpreet.flowdiary.navigation.NewRecordDestination
import fyi.manpreet.flowdiary.navigation.SettingsDestination
import fyi.manpreet.flowdiary.ui.home.HomeScreen
import fyi.manpreet.flowdiary.ui.newrecord.NewRecordScreen
import fyi.manpreet.flowdiary.ui.settings.SettingsScreen
import fyi.manpreet.flowdiary.ui.theme.FlowTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
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
                    onNewRecordClick = { path -> navController.navigate(NewRecordDestination(path = path.value)) },
                    onSettingsClick = { navController.navigate(SettingsDestination) }
                )
            }

            composable<NewRecordDestination> {
                val args = it.toRoute<NewRecordDestination>()
                NewRecordScreen(
                    navController = navController,
                    path = AudioPath(args.path),
                )
            }

            composable<SettingsDestination> {
                SettingsScreen(
                    navController = navController,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}
