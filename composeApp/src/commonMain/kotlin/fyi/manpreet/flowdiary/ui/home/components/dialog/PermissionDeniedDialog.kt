package fyi.manpreet.flowdiary.ui.home.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.common_cancel
import flowdiary.composeapp.generated.resources.permission_denied_title
import flowdiary.composeapp.generated.resources.permission_denied_message
import flowdiary.composeapp.generated.resources.permission_denied_settings
import fyi.manpreet.flowdiary.platform.permission.Permission
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import org.jetbrains.compose.resources.stringResource

// TODO Update design
@Composable
fun PermissionDeniedDialog(
    modifier: Modifier = Modifier,
    onSettingsClick: (HomeEvent.Permission) -> Unit,
    onDismissRequest: (HomeEvent.Permission) -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest(HomeEvent.Permission.Close) },
        title = {
            Text(text = stringResource(Res.string.permission_denied_title))
        },
        text = {
            Text(text = stringResource(Res.string.permission_denied_message))
        },
        confirmButton = {
            TextButton(
                onClick = { onSettingsClick(HomeEvent.Permission.Settings(Permission.MICROPHONE)) }
            ) {
                Text(text = stringResource(Res.string.permission_denied_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest(HomeEvent.Permission.Close) }) {
                Text(text = stringResource(Res.string.common_cancel))
            }
        }
    )
}
