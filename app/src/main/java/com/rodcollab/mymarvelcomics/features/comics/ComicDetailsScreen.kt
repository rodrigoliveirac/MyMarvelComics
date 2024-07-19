package com.rodcollab.mymarvelcomics.features.comics

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.ui.UiState
import com.rodcollab.mymarvelcomics.core.ui.components.CardContent
import com.rodcollab.mymarvelcomics.core.ui.components.LazyRowPaging
import com.rodcollab.mymarvelcomics.theme.ColorApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicDetailsScreen(
    onFavorite: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    uiState: UiState<Comic>,
    characters: LazyPagingItems<CharacterExternal>,
    navigateUp: () -> Unit,
) {


    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()

    var errorFromPaging by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = characters.loadState) {
        if (characters.loadState.refresh is LoadState.Error) {
            errorFromPaging = true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        bottomEnd = 32.dp,
                        bottomStart = 24.dp
                    )
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
                            text = "Comics"
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
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
                    .size(width = 240.dp, height = 200.dp)
            ) {
                CardContent(
                    onFavorite = onFavorite,
                    hideHeart = false,
                    id = uiState.model?.id ?: 0,
                    isFavorite = uiState.model?.isFavorite ?: false,
                    title = uiState.model?.title ?: "",
                    img = uiState.model?.urlImage ?: ""
                ) {}
            }

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                text = "Characters"
            )
            LazyRowPaging(
                modifier = Modifier.align(Alignment.CenterHorizontally), characters
            ) { character ->
                character?.let {
                    CardContent(
                        hideHeart = true,
                        id = character.id,
                        isFavorite = false,
                        title = character.name ?: "",
                        img = character.thumbnail,
                        toDetails = { })
                } ?: run {
                    characters.retry()
                }

            }
        }

    }

    if (uiState.askFirst != null) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            onDismissRequest = {
                onCancel()
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(68.dp),
                    painter = painterResource(id = R.drawable.desafiomarvel_spider_confirmation),
                    contentDescription = null
                )
                Text(style = MaterialTheme.typography.headlineMedium, text = "Are you sure?")
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = uiState.askFirst
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = ColorApp.favorite
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(4.dp),
                        onClick = onConfirm
                    ) {
                        Text(
                            color = ColorApp.favorite,
                            text = "Remove"
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorApp.favorite,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(4.dp),
                        onClick = onCancel
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            text = "Cancel"
                        )
                    }
                }
            }
        }
    }

}