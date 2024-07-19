package com.rodcollab.mymarvelcomics.features.characters

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.ui.UiState
import com.rodcollab.mymarvelcomics.core.ui.components.CardContent
import com.rodcollab.mymarvelcomics.core.ui.components.LazyRowPaging

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    onRefresh: () -> Unit,
    uiState: UiState<CharacterExternal>,
    comics: LazyPagingItems<Comic>,
    toComic: (Int) -> Unit,
    navigateUp: () -> Unit,
) {


    val context = LocalContext.current

    LaunchedEffect(key1 = comics.loadState) {
        if (comics.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (comics.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()

        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            onRefresh()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.clip(
                    RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 24.dp)
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = navigateUp) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Light,
                            text = "Character"
                        )
                    }
                })
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp, bottom = 16.dp, end = 16.dp)
                    .size(width = 240.dp, height = 180.dp)
            ) {
                CardContent(
                    hideHeart = true,
                    isFavorite = false,
                    id = uiState.model?.id ?: 0,
                    title = uiState.model?.name ?: "",
                    img = uiState.model?.thumbnail ?: ""
                ) {}
            }

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                text = "Comics"
            )
            LazyRowPaging(
                modifier = Modifier.align(Alignment.CenterHorizontally), comics
            ) { comic ->
                comic?.let {
                    CardContent(
                        hideHeart = !comic.isFavorite,
                        isFavorite = comic.isFavorite,
                        id = comic.id,
                        title = comic.title ?: "",
                        img = comic.urlImage,
                        toDetails = { comicId ->
                            toComic(comicId)
                        })
                } ?: run {
                    comics.retry()
                }

            }
        }

    }

}