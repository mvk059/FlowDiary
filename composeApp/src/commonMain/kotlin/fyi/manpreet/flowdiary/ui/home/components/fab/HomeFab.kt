package fyi.manpreet.flowdiary.ui.home.components.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.fab_cd
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import fyi.manpreet.flowdiary.ui.theme.FabShadow
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeFab(
    modifier: Modifier = Modifier,
    onFabClick: (HomeEvent) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.buttonPressed
        else MaterialTheme.gradient.button

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = MaterialTheme.spacing.fabContainerWidth,
                minHeight = MaterialTheme.spacing.fabContainerHeight,
            )
            .background(brush = gradient, shape = CircleShape)
            .shadow(
                elevation = MaterialTheme.spacing.smallMedium,
                spotColor = FabShadow,
                ambientColor = FabShadow,
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onFabClick(HomeEvent.RecordAudio) },
            ),
        contentAlignment = Alignment.Center,
    ) {

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(Res.string.fab_cd),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}