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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.common_cancel
import flowdiary.composeapp.generated.resources.common_confirm
import flowdiary.composeapp.generated.resources.ic_excited
import flowdiary.composeapp.generated.resources.ic_excited_outline
import flowdiary.composeapp.generated.resources.ic_neutal_outline
import flowdiary.composeapp.generated.resources.ic_neutral
import flowdiary.composeapp.generated.resources.ic_peaceful
import flowdiary.composeapp.generated.resources.ic_peaceful_outline
import flowdiary.composeapp.generated.resources.ic_sad
import flowdiary.composeapp.generated.resources.ic_sad_outline
import flowdiary.composeapp.generated.resources.ic_stressed
import flowdiary.composeapp.generated.resources.ic_stressed_outline
import flowdiary.composeapp.generated.resources.mood_excited
import flowdiary.composeapp.generated.resources.mood_neutral
import flowdiary.composeapp.generated.resources.mood_peaceful
import flowdiary.composeapp.generated.resources.mood_sad
import flowdiary.composeapp.generated.resources.mood_stressed
import flowdiary.composeapp.generated.resources.new_record_bottom_sheet_how_doing
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonDisabledNoRipple
import fyi.manpreet.flowdiary.ui.newrecord.components.button.ButtonPrimaryEnabledNoRipple
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.noRippleClickable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed interface EmotionType {
    data object Excited : EmotionType
    data object Peaceful : EmotionType
    data object Neutral : EmotionType
    data object Sad : EmotionType
    data object Stressed : EmotionType
}

data class EmotionIcon(
    val type: EmotionType,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
    val isSelected: Boolean = false,
    val contentDescription: StringResource,
)

@Composable
fun EmotionBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    onDismiss: () -> Unit,
) {

    val emotions = listOf(
        EmotionIcon(
            type = EmotionType.Excited,
            selectedIcon = Res.drawable.ic_excited,
            unselectedIcon = Res.drawable.ic_excited_outline,
            contentDescription = Res.string.mood_excited,
        ),
        EmotionIcon(
            type = EmotionType.Peaceful,
            selectedIcon = Res.drawable.ic_peaceful,
            unselectedIcon = Res.drawable.ic_peaceful_outline,
            contentDescription = Res.string.mood_peaceful,
        ),
        EmotionIcon(
            type = EmotionType.Neutral,
            selectedIcon = Res.drawable.ic_neutral,
            unselectedIcon = Res.drawable.ic_neutal_outline,
            contentDescription = Res.string.mood_neutral,
        ),
        EmotionIcon(
            type = EmotionType.Sad,
            selectedIcon = Res.drawable.ic_sad,
            unselectedIcon = Res.drawable.ic_sad_outline,
            contentDescription = Res.string.mood_sad,
        ),
        EmotionIcon(
            type = EmotionType.Stressed,
            selectedIcon = Res.drawable.ic_stressed,
            unselectedIcon = Res.drawable.ic_stressed_outline,
            contentDescription = Res.string.mood_stressed,
        ),
    )

    ModalBottomSheet(
        state = sheetState,
        onDismiss = onDismiss,
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

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.large),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {

                        emotions.fastForEach { item ->
                            IconButton(
                                modifier = Modifier
                                    .size(MaterialTheme.spacing.large3XL),
                                onClick = {},
                            ) {
                                Icon(
                                    painter = if (item.isSelected) painterResource(item.selectedIcon) else painterResource(
                                        item.unselectedIcon
                                    ),
                                    modifier = Modifier.size(MaterialTheme.spacing.large2XL),
                                    contentDescription = stringResource(item.contentDescription),
                                    tint = Color.Unspecified,
                                )
                            }
                        }
                    }

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
                                .noRippleClickable { onDismiss() }
                                .weight(0.3f)
                        ) {
                            Text(
                                text = stringResource(Res.string.common_cancel),
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        if (true) {
                            ButtonPrimaryEnabledNoRipple(
                                modifier = Modifier.weight(0.7f),
                                text = Res.string.common_confirm,
                                onClick = {},
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