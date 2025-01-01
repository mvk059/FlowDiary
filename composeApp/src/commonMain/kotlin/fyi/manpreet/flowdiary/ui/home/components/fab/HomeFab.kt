package fyi.manpreet.flowdiary.ui.home.components.fab

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.fab_cd
import fyi.manpreet.flowdiary.ui.theme.FabShadow
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeFab(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .shadow(
                elevation = MaterialTheme.spacing.smallMedium,
                spotColor = FabShadow,
                ambientColor = FabShadow,
                shape = CircleShape
            )
    ) {
        // FAB content
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(Res.string.fab_cd)
        )
    }
}