package fyi.manpreet.flowdiary.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.settings_appbar_title
import flowdiary.composeapp.generated.resources.settings_cd
import fyi.manpreet.flowdiary.ui.components.appbar.CenterTopAppBar
import fyi.manpreet.flowdiary.ui.settings.components.mood.SettingsMood
import fyi.manpreet.flowdiary.ui.settings.components.topic.TopicsSelection
import fyi.manpreet.flowdiary.ui.theme.Secondary90
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    navController: NavController,
    onBackClick: () -> Unit,
) {

    SettingsContent(
        onBackClick = onBackClick,
    )
}

@Composable
fun SettingsContent(
    onBackClick: () -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                text = stringResource(Res.string.settings_appbar_title),
                contentDescription = stringResource(Res.string.settings_cd),
                containerColor = Secondary90,
                onBackClick = onBackClick,
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(brush = MaterialTheme.gradient.background),
        ) {

            SettingsMood(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .shadow(
                        elevation = MaterialTheme.spacing.large,
                        shape = MaterialTheme.shapes.medium,
                        spotColor = Color(0xFF474F60).copy(alpha = 0.08f)
                    )
            )

            Spacer(Modifier.height(MaterialTheme.spacing.medium))

            TopicsSelection(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
        }
    }
}

