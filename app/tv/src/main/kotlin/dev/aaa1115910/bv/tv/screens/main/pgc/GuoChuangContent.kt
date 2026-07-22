package dev.aaa1115910.bv.tv.screens.main.pgc

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aaa1115910.biliapi.entity.pgc.PgcType
import dev.aaa1115910.bv.R
import dev.aaa1115910.bv.tv.activities.pgc.PgcIndexActivity
import dev.aaa1115910.bv.tv.activities.pgc.anime.AnimeTimelineActivity
import dev.aaa1115910.bv.ui.theme.BVTheme
import dev.aaa1115910.bv.viewmodel.pgc.PgcGuoChuangViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GuoChuangContent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    pgcViewModel: PgcGuoChuangViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val onOpenTimeline: () -> Unit = {
        context.startActivity(Intent(context, AnimeTimelineActivity::class.java))
    }
    val onOpenIndex: () -> Unit = {
        PgcIndexActivity.actionStart(context = context, pgcType = PgcType.GuoChuang)
    }

    PgcScaffold(
        lazyListState = lazyListState,
        pgcViewModel = pgcViewModel,
        pgcType = PgcType.GuoChuang,
        featureButtons = {
            GuoChuangFeatureButtons(
                modifier = Modifier.padding(vertical = 24.dp),
                onOpenTimeline = onOpenTimeline,
                onOpenIndex = onOpenIndex
            )
        }
    )
}

@Composable
private fun GuoChuangFeatureButtons(
    modifier: Modifier = Modifier,
    onOpenTimeline: () -> Unit,
    onOpenIndex: () -> Unit
) {
    val buttons = listOf(
        Triple(
            stringResource(R.string.anime_home_button_timeline),
            Icons.Rounded.Alarm,
            onOpenTimeline
        ),
        Triple(
            stringResource(R.string.anime_home_button_index),
            Icons.AutoMirrored.Rounded.List,
            onOpenIndex
        )
    )
    PgcFeatureButtons(
        modifier = modifier,
        buttons = buttons
    )
}

@Preview(device = "id:tv_1080p")
@Composable
private fun GuoChuangFeatureButtonsPreview() {
    BVTheme {
        GuoChuangFeatureButtons(
            modifier = Modifier,
            onOpenTimeline = {},
            onOpenIndex = {}
        )
    }
}
