package fyi.manpreet.flowdiary.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import fyi.manpreet.flowdiary.ui.home.components.appbar.HomeTopAppBar
import fyi.manpreet.flowdiary.ui.home.components.bottomsheet.RecordBottomSheet
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterOption
import fyi.manpreet.flowdiary.ui.home.components.chips.FilterScreen
import fyi.manpreet.flowdiary.ui.home.components.empty.HomeScreenEmpty
import fyi.manpreet.flowdiary.ui.home.components.fab.HomeFab
import fyi.manpreet.flowdiary.ui.home.components.list.AudioEntryContentItem
import fyi.manpreet.flowdiary.ui.home.components.list.TimelineItem
import fyi.manpreet.flowdiary.ui.home.state.HomeEvent
import fyi.manpreet.flowdiary.ui.theme.gradient
import fyi.manpreet.flowdiary.util.Peek
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavController,
) {
    val moodChip = viewModel.moodChip.collectAsStateWithLifecycle()
    val topicsChip = viewModel.topicsChip.collectAsStateWithLifecycle()

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

    HomeScreenContent(
        sheetState = sheetState,
        moodChip = moodChip.value,
        topicsChip = topicsChip.value,
        onMoodChipItemSelect = viewModel::onEvent,
        onTopicChipItemSelect = viewModel::onEvent,
        onMoodChipReset = viewModel::onEvent,
        onTopicChipReset = viewModel::onEvent,
        onBottomSheetShow = ::showBottomSheet,
        onBottomSheetDismiss = ::dismissBottomSheet,
    )

}

@Composable
fun HomeScreenContent(
    sheetState: ModalBottomSheetState,
    moodChip: FilterOption?,
    topicsChip: FilterOption?,
    onMoodChipItemSelect: (HomeEvent.Chip) -> Unit,
    onTopicChipItemSelect: (HomeEvent.Chip) -> Unit,
    onMoodChipReset: (HomeEvent.Chip) -> Unit,
    onTopicChipReset: (HomeEvent.Chip) -> Unit,
    onBottomSheetShow: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
) {

    val list = listOf(1, 2, 3)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopAppBar() },
        floatingActionButton = { HomeFab(onFabClick = onBottomSheetShow) },
    ) { innerPadding ->
        if (false) {
            HomeScreenEmpty(
                modifier = Modifier.padding(innerPadding)
                    .background(brush = MaterialTheme.gradient.background)
            )
            return@Scaffold
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .background(brush = MaterialTheme.gradient.background)
        ) {

            LazyColumn {

                item {
                    FilterScreen(
                        moodChip = moodChip,
                        topicsChip = topicsChip,
                        onMoodChipItemSelect = onMoodChipItemSelect,
                        onTopicChipItemSelect = onTopicChipItemSelect,
                        onMoodChipReset = onMoodChipReset,
                        onTopicChipReset = onTopicChipReset,
                    )
                }

                itemsIndexed(list) { index, item ->
                    TimelineItem(
                        isLastItem = index == list.lastIndex,
                    ) { modifier ->
                        AudioEntryContentItem(modifier)
                    }
                }
            }
        }
    }

    RecordBottomSheet(
        sheetState = sheetState,
        onDismiss = onBottomSheetDismiss
    )
}