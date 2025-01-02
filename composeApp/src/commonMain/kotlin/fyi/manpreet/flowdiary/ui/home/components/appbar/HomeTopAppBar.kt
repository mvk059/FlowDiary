package fyi.manpreet.flowdiary.ui.home.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.home_appbar_title
import flowdiary.composeapp.generated.resources.settings_cd
import fyi.manpreet.flowdiary.ui.theme.Secondary90
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.home_appbar_title),
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        actions = {
            IconButton(
                onClick = { },
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(Res.string.settings_cd),
                        tint = Color.Unspecified,
                    )
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Secondary90
        )
    )
}