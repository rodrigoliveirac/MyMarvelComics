package com.rodcollab.mymarvelcomics.features.characters

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.paging.compose.LazyPagingItems
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.ui.components.CardContent
import com.rodcollab.mymarvelcomics.core.ui.components.LazyVerticalGridPaging

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onModalNav: ()-> Unit,
    onRefresh: () -> Unit,
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

    DisposableEffect(key1 = Unit) {
        onRefresh()
        onDispose {}
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(modifier = Modifier.size(40.dp).padding(start = 16.dp),onClick = onModalNav) {
                            Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = null)
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Light,
                            text = "Characters"
                        )
                    }
            })
        },
        bottomBar = {

        }
    ) { paddingValues ->
        LazyVerticalGridPaging(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),characters) {character ->
            CardContent(hideHeart = true,isFavorite = false,id = character.id, title = character.name, img = character.thumbnail, toDetails = toDetails)
        }
    }

}