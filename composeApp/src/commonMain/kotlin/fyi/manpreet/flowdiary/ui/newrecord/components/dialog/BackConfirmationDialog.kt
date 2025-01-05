package fyi.manpreet.flowdiary.ui.newrecord.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.*
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.common_cancel
import flowdiary.composeapp.generated.resources.common_leave
import flowdiary.composeapp.generated.resources.new_record_leave_subtitle
import flowdiary.composeapp.generated.resources.new_record_leave_title
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackConfirmationDialog(
    modifier: Modifier = Modifier,
    onBack: (NewRecordEvent) -> Unit,
    onCancel: (NewRecordEvent) -> Unit
) {

    val dialogState = rememberDialogState(initiallyVisible = true)

    Box {

        Dialog(
            state = dialogState,
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            )
        ) {
            Scrim()
            DialogPanel(
                modifier = modifier
                    .displayCutoutPadding()
                    .systemBarsPadding()
                    .widthIn(max = MaterialTheme.spacing.mobileMaxWidthSize)    // Large size so that it fills the width in phones and shows smaller size in web
                    .wrapContentHeight()
                    .padding(MaterialTheme.spacing.large)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(MaterialTheme.spacing.small)
                    ),
            ) {
                Column {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.large)
                            .padding(top = MaterialTheme.spacing.large)
                    ) {
                        Text(
                            text = stringResource(Res.string.new_record_leave_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = stringResource(Res.string.new_record_leave_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(Modifier.height(MaterialTheme.spacing.small))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {

                        TextButton(
                            onClick = { onCancel(NewRecordEvent.BackConfirm(false)) },
                            content = {
                                Text(
                                    text = stringResource(Res.string.common_cancel),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        )

                        Spacer(Modifier.width(MaterialTheme.spacing.small))

                        TextButton(
                            onClick = {
                                dialogState.visible = false
                                onBack(NewRecordEvent.NavigateBack)
                            },
                            content = {
                                Text(
                                    text = stringResource(Res.string.common_leave),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        )

                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                    }
                }
            }
        }
    }
}
