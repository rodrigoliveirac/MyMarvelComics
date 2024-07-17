package com.rodcollab.mymarvelcomics.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.paging.compose.LazyPagingItems
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    characters: LazyPagingItems<CharacterExternal>
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = characters.loadState) {
        if(characters.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (characters.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(style = MaterialTheme.typography.headlineMedium, fontFamily = FontFamily.Serif, text = "Characters", color = MaterialTheme.colorScheme.primary)
            })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if(characters.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(count = characters.itemCount) { index ->
                        val character = characters[index]
                        Box(modifier = Modifier.padding(8.dp)
                            .clip(RoundedCornerShape(16.dp))) {
                            Card(
                                colors = CardDefaults.cardColors(Color.White),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .aspectRatio(0.8f)
                                    .clickable { }
                            ) {

                                var componentHeight by remember { mutableStateOf(0.dp) }

                                // get local density from composable
                                val density = LocalDensity.current

                                Box {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = componentHeight),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(character?.thumbnail)
                                            .build(),
                                        contentScale = ContentScale.FillBounds,
                                        contentDescription = null,
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Light,
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .onGloballyPositioned {
                                                componentHeight = with(density) {
                                                    it.size.height.toDp() - 10.dp
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
                                        text = character?.name ?: "",
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                    item {
                        if(characters.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

}