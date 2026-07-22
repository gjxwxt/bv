package dev.aaa1115910.bv.tv.component.search

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.DenseListItem
import androidx.tv.material3.Text
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun SearchKeyword(
    modifier: Modifier = Modifier,
    keyword: String,
    leadingIcon: String = "",
    rank: Int? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(data = leadingIcon)
            .size(Size.ORIGINAL)
            .build(),
        imageLoader = imageLoader,
        contentScale = ContentScale.FillHeight
    )

    DenseListItem(
        modifier = modifier,
        selected = false,
        onClick = onClick,
        headlineContent = {
            Text(
                text = keyword,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            if (rank != null) {
                val rankColor = when (rank) {
                    1 -> Color(0xFFFF4D4F)
                    2 -> Color(0xFFFF7A45)
                    3 -> Color(0xFFFFA940)
                    else -> Color.Gray.copy(alpha = 0.7f)
                }
                Text(
                    text = "$rank",
                    color = rankColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
            } else if (leadingIcon.isNotEmpty() && painter.state is AsyncImagePainter.State.Success) {
                Image(
                    modifier = Modifier.height(16.dp),
                    painter = painter,
                    contentDescription = null,
                )
            }
        },
        trailingContent = trailingIcon
    )
}