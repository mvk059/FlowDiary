package fyi.manpreet.flowdiary.ui.newrecord.components.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MiniTextField(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    hintText: StringResource,
    imeAction: ImeAction = ImeAction.Next,
) {

    var textFieldValue by remember { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(icon),
            modifier = Modifier.size(MaterialTheme.spacing.medium),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant),
        )

        BasicTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            modifier = Modifier.padding(start = MaterialTheme.spacing.small),
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLines = 10,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = imeAction,
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (textFieldValue.isEmpty()) {
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
