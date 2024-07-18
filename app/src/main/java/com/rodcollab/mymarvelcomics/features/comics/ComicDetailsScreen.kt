package com.rodcollab.mymarvelcomics.features.comics

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicDetailsScreen(
    uiState: UiState<Comic>,
    characters: LazyPagingItems<CharacterExternal>,
    navigateUp: () -> Unit
) {


    val context = LocalContext.current

    LaunchedEffect(key1 = characters.loadState) {
        if (characters.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (characters.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()

        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = navigateUp) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Light,
                            text = "Comic"
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
                    .padding(16.dp)
                    .size(width = 240.dp, height = 180.dp)
            ) {
                CardContent(
                    id = uiState.model?.id ?: 0,
                    title = uiState.model?.title ?: "",
                    img = uiState.model?.urlImage ?: ""
                ) {}
            }

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                color = Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                text = uiState.model?.description ?: ""
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                text = "characters".uppercase(
                    Locale.ROOT
                )
            )
            LazyRowPaging(
                modifier = Modifier, characters
            ) { character ->
                CardContent(
                    id = character.id,
                    title = character.name ?: "",
                    img = character.thumbnail,
                    toDetails = { })
            }
        }

    }

}