package com.rodcollab.mymarvelcomics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCNavGraph
import com.rodcollab.mymarvelcomics.features.characters.CharactersViewModel
import com.rodcollab.mymarvelcomics.features.characters.CharactersScreen
import com.rodcollab.mymarvelcomics.theme.MyMarvelComicsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMarvelComicsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MMCNavGraph()
                }
            }
        }
    }
}