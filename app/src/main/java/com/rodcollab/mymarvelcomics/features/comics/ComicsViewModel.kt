package com.rodcollab.mymarvelcomics.features.comics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.domain.ComicDomain
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.ui.UiState
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ComicsSession(val title: String) {
    FAVORITES(title = "Favorites"),
    ALL(title = "Comics")
}

data class UiOption<T>(
    val model: T,
    val defImg: Int,
    val isSelected: Boolean,
) {
    val img = if (isSelected) R.drawable.ic_done else defImg
}

data class Options<T>(
    val items: List<T>,
    val expanded: Boolean,
    val selectedItem: T,
)

@HiltViewModel
class ComicsViewModel @Inject constructor(private val domain: ComicDomain) :
    ViewModel() {

    var allComics = domain.comics().cachedIn(viewModelScope)

    private val _favorites = MutableStateFlow<UiState<List<Comic>>>(UiState())
    val favorites = _favorites.asStateFlow()

    private val allOption =
        UiOption(model = ComicsSession.ALL, defImg = R.drawable.ic_list, isSelected = true)
    private val favoritesOption = UiOption(
        model = ComicsSession.FAVORITES,
        defImg = R.drawable.ic_favorite,
        isSelected = false
    )

    private val _dropDownMenu = MutableStateFlow(
        Options(
            items = listOf(allOption, favoritesOption),
            selectedItem = allOption,
            expanded = false
        )
    )

    val dropDownMenu = _dropDownMenu.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            fetchFavorites(
                withResult = {
                    updateSession(it)
                },
                withError =  {
                    Log.d("COMICS_VIEWMODEL", it)
                }
            )
        }
    }

    fun switchSession(sessionSelected: ComicsSession) {
        viewModelScope.launch {
            if (sessionSelected != _dropDownMenu.value.selectedItem.model) {
                when (sessionSelected) {
                    ComicsSession.FAVORITES -> {
                        fetchFavorites(
                            withResult = {
                                val uiOptionsUpdated = uiOptionsUpdated(sessionSelected)
                                updateDropDownMenu(uiOptionsUpdated,sessionSelected)
                            },
                            withError = {
                                Log.d("COMICS_VIEWMODEL",it)
                            }
                        )
                    }

                    else -> {
                        val options = uiOptionsUpdated(sessionSelected)
                        updateDropDownMenu(options, sessionSelected)
                    }
                }

            }
        }
    }

    private suspend fun fetchFavorites(withResult: (List<Comic>)-> Unit, withError:(String)-> Unit) {
        domain.favorites { resultOf ->
            when (resultOf) {
                is ResultOf.Success -> {
                    withResult(resultOf.value)
                }
                is ResultOf.Failure -> {
                    withError(resultOf.message.toString())
                }
            }
        }
    }

    private fun updateDropDownMenu(
        options: List<UiOption<ComicsSession>>,
        sessionSelected: ComicsSession,
    ) {
        _dropDownMenu.update {
            it.copy(
                expanded = !it.expanded,
                items = options,
                selectedItem = it.selectedItem.copy(sessionSelected)
            )
        }
    }

    private fun uiOptionsUpdated(sessionSelected: ComicsSession) =
        _dropDownMenu.value.items.map {
            if (it.model == sessionSelected) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }

    private fun updateSession(comics: List<Comic>) {
        _favorites.update {
            it.copy(model = comics)
        }
    }

    fun expandedDropdownMenu() {
        viewModelScope.launch {
            _dropDownMenu.update {
                it.copy(expanded = !it.expanded)
            }
        }
    }

}