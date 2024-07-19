package com.rodcollab.mymarvelcomics.features.characters

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.paging.compose.LazyPagingItems
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.ui.components.CardContent
import com.rodcollab.mymarvelcomics.core.ui.components.LazyVerticalGridPaging
import com.rodcollab.mymarvelcomics.theme.ColorApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    toComics: (String) -> Unit,
    onRefresh: () -> Unit,
    characters: LazyPagingItems<CharacterExternal>,
    toDetails: (Int) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()

    var errorFromPaging by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = characters.loadState) {
        if (characters.loadState.refresh is LoadState.Error) {
            errorFromPaging = true
        }
    }

    DisposableEffect(key1 = Unit) {
        onRefresh()
        onDispose {}
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Image(
                    modifier = Modifier
                        .heightIn(max = 80.dp)
                        .padding(start = 16.dp),
                    painter = painterResource(id = R.drawable.marvel_logo),
                    contentDescription = null
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = NavItem.Comics.label) },
                    selected = false,
                    onClick = { toComics(NavItem.Comics.route) }
                )
                NavigationDrawerItem(
                    label = { Text(text = NavItem.Characters.label) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        },
    ) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    ),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(modifier = Modifier
                                .size(40.dp)
                                .padding(start = 16.dp), onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = null
                                )
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
            }
        ) { paddingValues ->
            LazyVerticalGridPaging(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), characters
            ) { character ->
                character?.let {
                    CardContent(
                        hideHeart = true,
                        isFavorite = false,
                        id = character.id,
                        title = character.name,
                        img = character.thumbnail,
                        toDetails = toDetails
                    )
                } ?: run {
                    characters.retry()
                }

            }
        }

    }

    if (errorFromPaging) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            onDismissRequest = {
                scope.launch {
                    sheetState.partialExpand()
                }
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.timeout_error),
                    contentDescription = null
                )
                Text(textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall, text = stringResource(R.string.oh_no_something_has_gone_wrong)
                )

                Row(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = ColorApp.favorite
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(4.dp),
                        onClick = {
                            errorFromPaging = false
                            characters.retry()
                        }
                    ) {
                        Text(
                            color = ColorApp.favorite,
                            text = stringResource(R.string.try_again)
                        )
                    }
                }
            }
        }
    }
}