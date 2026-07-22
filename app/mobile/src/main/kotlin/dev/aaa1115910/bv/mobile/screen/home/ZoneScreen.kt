package dev.aaa1115910.bv.mobile.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.aaa1115910.bv.mobile.activities.VideoPlayerActivity
import dev.aaa1115910.bv.mobile.component.videocard.SmallVideoCard
import dev.aaa1115910.bv.entity.carddata.VideoCardData
import dev.aaa1115910.bv.util.OnBottomReached
import dev.aaa1115910.bv.viewmodel.ugc.UgcAnimalViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcCarViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcCinephileViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcDanceViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcDougaViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcEntViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcFashionViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcFoodViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcGameViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcKichikuViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcKnowledgeViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcMusicViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcSportsViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcTechViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcViewModel
import dev.aaa1115910.bv.viewmodel.ugc.UgcVlogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private data class ZoneTabItem(
    val title: String,
    val getViewModel: @Composable () -> UgcViewModel
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZoneScreen(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val zoneTabs = listOf(
        ZoneTabItem("动画") { koinViewModel<UgcDougaViewModel>() },
        ZoneTabItem("游戏") { koinViewModel<UgcGameViewModel>() },
        ZoneTabItem("科技") { koinViewModel<UgcTechViewModel>() },
        ZoneTabItem("音乐") { koinViewModel<UgcMusicViewModel>() },
        ZoneTabItem("舞蹈") { koinViewModel<UgcDanceViewModel>() },
        ZoneTabItem("美食") { koinViewModel<UgcFoodViewModel>() },
        ZoneTabItem("动物") { koinViewModel<UgcAnimalViewModel>() },
        ZoneTabItem("知识") { koinViewModel<UgcKnowledgeViewModel>() },
        ZoneTabItem("鬼畜") { koinViewModel<UgcKichikuViewModel>() },
        ZoneTabItem("娱乐") { koinViewModel<UgcEntViewModel>() },
        ZoneTabItem("影视") { koinViewModel<UgcCinephileViewModel>() },
        ZoneTabItem("汽车") { koinViewModel<UgcCarViewModel>() },
        ZoneTabItem("时尚") { koinViewModel<UgcFashionViewModel>() },
        ZoneTabItem("运动") { koinViewModel<UgcSportsViewModel>() },
        ZoneTabItem("VLOG") { koinViewModel<UgcVlogViewModel>() }
    )

    val pageState = rememberPagerState(pageCount = { zoneTabs.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        ScrollableTabRow(
            modifier = Modifier.zIndex(1f),
            selectedTabIndex = pageState.currentPage,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            edgePadding = 12.dp
        ) {
            zoneTabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pageState.currentPage == index,
                    onClick = {
                        scope.launch { pageState.animateScrollToPage(index) }
                    },
                    text = { Text(text = tab.title) }
                )
            }
        }

        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val viewModel = zoneTabs[page].getViewModel()
            ZoneCategoryPage(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ZoneCategoryPage(
    viewModel: UgcViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.updating,
        onRefresh = {
            viewModel.reloadAll()
        }
    )

    gridState.OnBottomReached(loading = viewModel.updating) {
        scope.launch(Dispatchers.IO) {
            viewModel.loadMore()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(180.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.ugcItems) { video ->
                SmallVideoCard(
                    data = VideoCardData(
                        avid = video.aid,
                        title = video.title,
                        cover = video.cover,
                        play = video.play,
                        danmaku = video.danmaku,
                        upName = video.author,
                        time = video.duration * 1000L
                    ),
                    onClick = {
                        VideoPlayerActivity.actionStart(context = context, aid = video.aid)
                    }
                )
            }
        }

        PullRefreshIndicator(
            refreshing = viewModel.updating,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
