package com.rodcollab.mymarvelcomics.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.theme.ColorApp

@Composable
internal fun <T : Any> LazyRowPaging(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    content: @Composable ColumnScope.(T?) -> Unit,
) {
    Box(modifier = modifier) {
        if (items.loadState.refresh is LoadState.Loading) {
            LoadingWithDeadPool(modifier = modifier)
        } else {
            LazyRow(
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(count = items.itemCount) { index ->
                    val item = items[index] as T?
                    CardScaffold(
                        Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .size(150.dp)) {
                        content(item)
                    }
                }
            }
        }
    }
}

@Composable
internal fun <T : Any> LazyVerticalGridPaging(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    content: @Composable ColumnScope.(T?) -> Unit,
) {
    Box(modifier = modifier) {
        if (items.loadState.refresh is LoadState.Loading) {
            LoadingWithDeadPool(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(
                contentPadding = PaddingValues(8.dp),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize()
            ) {
                items(count = items.itemCount) { index ->
                    val item = items[index] as T?
                    CardScaffold(
                        Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .aspectRatio(0.8f)) {
                        content(item)
                    }
                }
            }
        }
        if (items.loadState.append is LoadState.Loading) {
            LoadingMoreItemsIndicator(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
internal fun CardScaffold(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Card(
            colors = CardDefaults.cardColors(Color.White),
            modifier = modifier
        ) {

            content()
        }
    }
}

@Composable
internal fun CardContent(
    hideHeart: Boolean,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    id: Int,
    title: String,
    img: String?,
    onFavorite: () -> Unit = { },
    toDetails: (Int) -> Unit,
) {
    var componentHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Box(modifier = modifier.clickable { toDetails(id) }) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = componentHeight + 5.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(img)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
            if(!hideHeart) {
                Button(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(36.dp)
                        .zIndex(1f)
                        .background(
                            shape = RoundedCornerShape(36.dp),
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Transparent,
                                    0.0f to Color.White,
                                    1f to Color.LightGray
                                )
                            )
                        ).clip(RoundedCornerShape(36.dp)),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = { onFavorite() }) {
                    Icon(tint = ColorApp.favorite, painter = painterResource(id = if(isFavorite)  R.drawable.ic_favorite else  R.drawable.ic_unfavorite), contentDescription = null)
                }
            }
        }

        Text(
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(top = 16.dp)
                .onGloballyPositioned {
                    componentHeight = with(density) {
                        it.size.height.toDp() - 2.dp
                    }
                }
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.4f to Color.White,
                            1f to Color.LightGray
                        )
                    )
                )
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            text = title ?: "",
            maxLines = 1
        )

    }
}

@Composable
internal fun LoadingMoreItemsIndicator(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        modifier = modifier
            .heightIn(max = 2.dp)
            .fillMaxWidth()
    )
}