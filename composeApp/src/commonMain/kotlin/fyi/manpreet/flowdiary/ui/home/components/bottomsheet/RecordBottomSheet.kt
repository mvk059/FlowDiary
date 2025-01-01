package fyi.manpreet.flowdiary.ui.home.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.close_cd
import flowdiary.composeapp.generated.resources.play_cd
import flowdiary.composeapp.generated.resources.record_bottom_sheet_recording
import fyi.manpreet.flowdiary.ui.home.components.fab.GradientFAB
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecordBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    onDismiss: () -> Unit,
) {

    val isPlaying = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // TODO Add to main fab.
    val gradient =
        if (isPressed) MaterialTheme.gradient.buttonPressed
        else MaterialTheme.gradient.button

    ModalBottomSheet(
        state = sheetState,
        onDismiss = onDismiss,
    ) {

        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.large),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal).asPaddingValues())
                    .navigationBarsPadding()
                    .imePadding(),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    DragIndication(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.large)
                            .background(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.large
                            )
                            .width(MaterialTheme.spacing.largeXL)
                            .height(MaterialTheme.spacing.extraSmall)
                    )

                    Text(
                        text = stringResource(Res.string.record_bottom_sheet_recording),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = "01:30:45",
                        modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .heightIn(min = 150.dp)
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.largeXL
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {

                        IconButton(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.errorContainer, shape = CircleShape)
                                .size(MaterialTheme.spacing.large2XL),
                            onClick = {},
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.close_cd),
                                    tint = Color.Unspecified,
                                )
                            }
                        )

                        if (isPlaying.value) {
                            GradientFAB { isPlaying.value = false }
                        } else {
                            IconButton(
                                modifier = Modifier
                                    .background(brush = MaterialTheme.gradient.button, shape = CircleShape)
                                    .size(MaterialTheme.spacing.large3XL)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = {},
                                    ),
                                onClick = { isPlaying.value = true },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = stringResource(Res.string.close_cd), // TODO Change
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            )
                        }

                        IconButton(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.inverseOnSurface, shape = CircleShape)
                                .size(MaterialTheme.spacing.large2XL),
                            onClick = {},
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = stringResource(Res.string.play_cd),
                                    tint = Color.Unspecified,
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
