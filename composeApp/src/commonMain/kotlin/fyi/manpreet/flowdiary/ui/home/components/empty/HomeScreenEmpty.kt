package fyi.manpreet.flowdiary.ui.home.components.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.home_empty_subtitle
import flowdiary.composeapp.generated.resources.home_empty_title
import flowdiary.composeapp.generated.resources.ic_empty
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreenEmpty(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(Res.drawable.ic_empty),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(top = MaterialTheme.spacing.large),
            text = stringResource(Res.string.home_empty_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall),
            text = stringResource(Res.string.home_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
