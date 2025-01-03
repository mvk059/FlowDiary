package fyi.manpreet.flowdiary.ui.home.components.list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.list_item_show_less
import flowdiary.composeapp.generated.resources.list_item_show_more
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    collapsedMaxLine: Int = 3,
    showMoreText: String = stringResource(Res.string.list_item_show_more),
    showMoreStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.W500
    ),
    showLessText: String = stringResource(Res.string.list_item_show_less),
    showLessStyle: SpanStyle = showMoreStyle,
) {

    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }

    val annotatedText = buildAnnotatedString {
        if (clickable) {
            if (isExpanded) {
                // Display the full text and "Show Less" button when expanded.
                append(text)
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Show Less",
                        linkInteractionListener = { isExpanded = !isExpanded }
                    )
                ) {
                    withStyle(style = showLessStyle) { append(showLessText) }
                }
            } else {
                // Display truncated text and "Show More" button when collapsed.
                val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreText.length)
                    .dropLastWhile { it.isWhitespace() || it == '.' }
                append(adjustText)
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Show More",
                        linkInteractionListener = { isExpanded = !isExpanded }
                    )
                ) {
                    withStyle(style = showMoreStyle) { append(showMoreText) }
                }
            }
        } else {
            // Display the full text when not clickable.
            append(text)
        }
    }

    Box(
        modifier = modifier
    ) {

        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = annotatedText,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            // Determine visual overflow and enable click ability.
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
