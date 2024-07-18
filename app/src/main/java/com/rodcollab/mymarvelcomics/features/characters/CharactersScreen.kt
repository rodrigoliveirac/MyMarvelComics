package com.rodcollab.mymarvelcomics.features.characters

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.paging.compose.LazyPagingItems
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.paging.LoadState
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.ui.components.CardContent
import com.rodcollab.mymarvelcomics.core.ui.components.PagingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    characters: LazyPagingItems<CharacterExternal>,
    toDetails: (Int) -> Unit
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
            CenterAlignedTopAppBar(

                title = {
                Text(style = MaterialTheme.typography.headlineMedium, fontFamily = FontFamily.Serif, text = "CHARACTERS")
            })
        },
        bottomBar = {

        }
    ) { paddingValues ->
        PagingContent(modifier = Modifier.fillMaxSize().padding(paddingValues),characters) {character ->
            CardContent(id = character.id, title = character.name, img = character.thumbnail, toDetails = toDetails)
        }
    }

}