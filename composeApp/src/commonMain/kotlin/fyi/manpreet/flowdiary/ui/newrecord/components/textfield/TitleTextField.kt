package fyi.manpreet.flowdiary.ui.newrecord.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.new_record_add_icon_cd
import flowdiary.composeapp.generated.resources.new_record_add_title
import fyi.manpreet.flowdiary.data.mapper.toIcon
import fyi.manpreet.flowdiary.ui.components.emotion.EmotionType
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.theme.Secondary70
import fyi.manpreet.flowdiary.ui.theme.Secondary95
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitleTextField(
    modifier: Modifier = Modifier,
    title: String?,
    emotionType: EmotionType?,
    onAddClick: (NewRecordEvent.FabBottomSheet) -> Unit,
    onTitleUpdate: (NewRecordEvent.Data) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    TextField(
        value = title ?: "",
        onValueChange = { onTitleUpdate(NewRecordEvent.Data.UpdateTitle(it)) },
        modifier = modifier.fillMaxWidth().focusRequester(focusRequester),
        leadingIcon = {
            if (emotionType == null) {
                IconButton(
                    onClick = { onAddClick(NewRecordEvent.FabBottomSheet.SheetShow) },
                    modifier = Modifier
                        .background(color = Secondary95, shape = CircleShape)
                        .size(MaterialTheme.spacing.largeXL),
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(Res.string.new_record_add_icon_cd),
                            tint = Secondary70,
                        )
                    }
                )
            } else {
                IconButton(
                    onClick = { onAddClick(NewRecordEvent.FabBottomSheet.SheetShow) },
                    modifier = Modifier
                        .background(color = Secondary95, shape = CircleShape)
                        .size(MaterialTheme.spacing.largeXL),
                    content = {
                        Icon(
                            painter = painterResource(emotionType.toIcon()),
                            modifier = Modifier.size(MaterialTheme.spacing.largeXL),
                            contentDescription = stringResource(Res.string.new_record_add_icon_cd),
                            tint = Color.Unspecified,
                        )
                    }
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(Res.string.new_record_add_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineLarge,
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
    )
}
