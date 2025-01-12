package fyi.manpreet.flowdiary.ui.newrecord.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalSheetProperties
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.common_cancel
import flowdiary.composeapp.generated.resources.common_confirm
import flowdiary.composeapp.generated.resources.new_record_bottom_sheet_how_doing
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionRow
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.components.emotion.Emotions
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonDisabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonPrimaryEnabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.Peek
import fyi.manpreet.flowdiary.util.noRippleClickable
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmotionBottomSheet(
    modifier: Modifier = Modifier,
    emotionType: EmotionType?,
    emotions: List<Emotions>?,
    emotionsSaveButtonEnabled: Boolean,
    fabBottomSheet: NewRecordEvent.FabBottomSheet?,
    onEmotionTypeSelect: (NewRecordEvent.Data) -> Unit,
    onDismiss: (NewRecordEvent.FabBottomSheet) -> Unit,
) {

    if (emotions.isNullOrEmpty()) return
    var selectedIcon by remember { mutableStateOf(emotionType) }

    val sheetState = rememberModalBottomSheetState(
        initialDetent = if (fabBottomSheet == NewRecordEvent.FabBottomSheet.SheetHide) Hidden else Peek,
        detents = listOf(Hidden, Peek),
    )

    if (fabBottomSheet == NewRecordEvent.FabBottomSheet.SheetShow) sheetState.currentDetent = Peek
    else sheetState.currentDetent = Hidden

    ModalBottomSheet(
        state = sheetState,
        properties = ModalSheetProperties().copy(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        onDismiss = { onDismiss(NewRecordEvent.FabBottomSheet.SheetHide) },
    ) {

        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.large
                )
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(
                        topStart = MaterialTheme.spacing.large,
                        topEnd = MaterialTheme.spacing.large
                    )
                ),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.bottomSheetMaxHeight)
                    .navigationBarsPadding(),
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
                            .padding(
                                top = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.large
                            )
                            .background(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.large
                            )
                            .width(MaterialTheme.spacing.largeXL)
                            .height(MaterialTheme.spacing.extraSmall)
                    )

                    Text(
                        text = stringResource(Res.string.new_record_bottom_sheet_how_doing),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    EmotionRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.large),
                        emotions = emotions,
                        onClick = {
                            selectedIcon = it
                            onEmotionTypeSelect(NewRecordEvent.Data.UpdateEmotion(selectedIcon!!))
                        },
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(top = MaterialTheme.spacing.largeXL),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    ) {

                        Box(
                            modifier = Modifier
                                .defaultMinSize(
                                    minWidth = ButtonDefaults.MinWidth,
                                    minHeight = ButtonDefaults.MinHeight
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    shape = MaterialTheme.shapes.large
                                )
                                .noRippleClickable { onDismiss(NewRecordEvent.FabBottomSheet.SheetHide) }
                                .weight(0.3f)
                        ) {
                            Text(
                                text = stringResource(Res.string.common_cancel),
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        if (emotionsSaveButtonEnabled) {
                            ButtonPrimaryEnabledNoRipple(
                                modifier = Modifier.weight(0.7f),
                                text = Res.string.common_confirm,
                                onClick = {
                                    if (selectedIcon != null)
                                        onEmotionTypeSelect(NewRecordEvent.Data.SaveEmotion(selectedIcon!!))
                                },
                                weight = 0.7f,
                            )
                        } else {
                            ButtonDisabledNoRipple(
                                modifier = Modifier.weight(0.7f),
                                text = Res.string.common_confirm,
                                onClick = {},
                                weight = 0.7f,
                            )
                        }
                    }
                }
            }
        }
    }
}
