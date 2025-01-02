package fyi.manpreet.flowdiary.ui.newrecord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import flowdiary.composeapp.generated.resources.Res
import flowdiary.composeapp.generated.resources.ic_edit
import flowdiary.composeapp.generated.resources.ic_hashtag
import flowdiary.composeapp.generated.resources.new_record_add_description
import flowdiary.composeapp.generated.resources.new_record_add_topic
import fyi.manpreet.flowdiary.ui.newrecord.components.appbar.NewRecordTopAppBar
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.MiniTextField
import fyi.manpreet.flowdiary.ui.newrecord.components.textfield.TitleTextField
import fyi.manpreet.flowdiary.ui.theme.spacing
import fyi.manpreet.flowdiary.util.Peek
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewRecordScreen(
    viewModel: NewRecordViewModel = koinViewModel(),
    navController: NavController,
    onBackClick: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek)
    )

    fun dismissBottomSheet() {
        sheetState.currentDetent = Hidden
    }

    fun showBottomSheet() {
        sheetState.currentDetent = Peek
    }

    NewRecordScreenContent(
        sheetState = sheetState,
        onBackClick = onBackClick,
    )
}

@Composable
fun NewRecordScreenContent(
    sheetState: ModalBottomSheetState,
    onBackClick: () -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { NewRecordTopAppBar(onBackClick = onBackClick) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.onPrimary),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            TitleTextField(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
            MiniTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                icon = Res.drawable.ic_hashtag,
                hintText = Res.string.new_record_add_topic,

            )
            MiniTextField(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                icon = Res.drawable.ic_edit,
                hintText = Res.string.new_record_add_description,
                imeAction = ImeAction.Done,
            )
        }
    }
}