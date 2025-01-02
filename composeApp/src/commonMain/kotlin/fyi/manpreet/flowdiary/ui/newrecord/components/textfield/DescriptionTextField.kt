package fyi.manpreet.flowdiary.ui.newrecord.components.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_hashtag
import flowdiary.composeapp.generated.resources.new_record_add_description
import flowdiary.composeapp.generated.resources.new_record_add_description_cd
import fyi.manpreet.flowdiary.ui.theme.Secondary70
import fyi.manpreet.flowdiary.ui.theme.Secondary95
import fyi.manpreet.flowdiary.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DescriptionTextField(
    modifier: Modifier = Modifier,
) {

    var textFieldValue by remember { mutableStateOf("") }

    // TODO Change to BasicTextField and check
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            imageVector = Icons.Default.Edit,
            modifier = Modifier.size(MaterialTheme.spacing.medium),
            contentDescription = stringResource(Res.string.new_record_add_description_cd),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant),
        )

        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            placeholder = {
                Text(
                    text = stringResource(Res.string.new_record_add_description),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLines = 10,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
        )
    }
}
