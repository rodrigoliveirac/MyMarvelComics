package com.rodcollab.mymarvelcomics.features.comics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.SheetState
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
import androidx.paging.compose.LazyPagingItems
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.ui.components.CardContent
import com.rodcollab.mymarvelcomics.core.ui.components.CardScaffold
import com.rodcollab.mymarvelcomics.core.ui.components.LazyVerticalGridPaging
import com.rodcollab.mymarvelcomics.features.characters.NavItem
import com.rodcollab.mymarvelcomics.theme.ColorApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicsScreen(
    onRefresh:()-> Unit,
    dropDownExpanded: Boolean,
    expandedDropdownMenu: () -> Unit,
    toFilter: (ComicsSession) -> Unit,
    toCharacters: (String) -> Unit,
    uiOptions: Options<UiOption<ComicsSession>>,
    favorites: List<Comic>,
    comics: LazyPagingItems<Comic>,
    toDetails: (Int) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()
    var errorFromPaging by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = comics.loadState) {
        if (comics.loadState.refresh is LoadState.Error) {
            errorFromPaging = true
        }
    }

    DisposableEffect(key1 = Unit) {
        onRefresh()
        onDispose {  }
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
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = NavItem.Characters.label) },
                    selected = false,
                    onClick = {
                        toCharacters(NavItem.Characters.route)
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
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Light,
                                text = uiOptions.selectedItem.model.title
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 16.dp), onClick = expandedDropdownMenu
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_filter),
                                        contentDescription = null
                                    )
                                }
                                DropdownMenu(
                                    modifier = Modifier.background(Color.White),
                                    expanded = dropDownExpanded,
                                    onDismissRequest = expandedDropdownMenu,
                                ) {
                                    uiOptions.items.forEach { label ->
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    tint = ColorApp.favorite,
                                                    painter = painterResource(id = label.img),
                                                    contentDescription = null
                                                )
                                            },
                                            text = {
                                                Text(
                                                    color = Color.DarkGray,
                                                    text = label.model.title
                                                )
                                            },
                                            onClick = {
                                                toFilter(label.model)
                                            })
                                    }
                                }
                            }
                        }
                    })
            }
        ) { paddingValues ->

            when (uiOptions.selectedItem.model) {
                ComicsSession.ALL -> {
                    LazyVerticalGridPaging(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues), comics
                    ) { comic ->
                        comic?.let {
                            CardContent(
                                hideHeart = true,
                                isFavorite = comic.isFavorite,
                                id = comic.id,
                                title = comic.title ?: "",
                                img = comic.urlImage,
                                toDetails = toDetails
                            )
                        } ?: run {
                            comics.retry()
                        }

                    }
                }

                else -> {
                    LazyVerticalGrid(
                        contentPadding = PaddingValues(8.dp),
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(items = favorites) { comic ->
                            CardScaffold(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .aspectRatio(0.8f),
                            ) {
                                CardContent(
                                    hideHeart = true,
                                    isFavorite = comic.isFavorite,
                                    id = comic.id,
                                    title = comic.title ?: "",
                                    img = comic.urlImage,
                                    toDetails = toDetails
                                )
                            }
                        }
                    }
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
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    text = stringResource(R.string.oh_no_something_has_gone_wrong)
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
                        onClick = {
                            errorFromPaging = false
                            comics.refresh()
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

@OptIn(ExperimentalMaterial3Api::class)
fun hideBottomSheet(
    scope: CoroutineScope,
    sheetState: SheetState,
    showBottomSheet: (Boolean) -> Unit,
) {
    scope.launch { sheetState.hide() }.invokeOnCompletion {
        if (!sheetState.isVisible) {
            showBottomSheet(false)
        }
    }
}