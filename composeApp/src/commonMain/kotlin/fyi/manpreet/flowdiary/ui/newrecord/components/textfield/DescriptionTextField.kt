package fyi.manpreet.flowdiary.ui.newrecord.components.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import fyi.manpreet.flowdiary.ui.newrecord.state.NewRecordEvent
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DescriptionTextField(
    modifier: Modifier = Modifier,
    descriptionText: String,
    onDescriptionUpdate: (NewRecordEvent.Data) -> Unit,
    icon: DrawableResource,
    hintText: StringResource,
    imeAction: ImeAction = ImeAction.Done,
    descriptionFieldFocusRequester: FocusRequester,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {

        Image(
            painter = painterResource(icon),
            modifier = Modifier.size(MaterialTheme.spacing.large),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant),
        )

        BasicTextField(
            value = descriptionText,
            onValueChange = { onDescriptionUpdate(NewRecordEvent.Data.UpdateDescription(it)) },
            modifier = Modifier
                .padding(
                    start = MaterialTheme.spacing.small,
                    top = MaterialTheme.spacing.extraSmall
                )
                .focusRequester(descriptionFieldFocusRequester),
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLines = 10,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = imeAction,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    descriptionFieldFocusRequester.freeFocus()
                }
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (descriptionText.isEmpty()) {
                        Text(
                            text = stringResource(hintText),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
