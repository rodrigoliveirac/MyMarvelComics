package com.rodcollab.mymarvelcomics.features.comics

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
import kotlinx.coroutines.flow.cancellable
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

    private var _allComics = domain.comics().cachedIn(viewModelScope)
    val allComics = _allComics
    fun refresh() {
        when (_dropDownMenu.value.selectedItem.model) {
            ComicsSession.FAVORITES -> {
                viewModelScope.launch {
                    domain.favorites { resultOf ->
                        when (resultOf) {
                            is ResultOf.Success -> {
                                _favorites.update {
                                    it.copy(model = resultOf.value)
                                }
                                _allComics = _allComics.cancellable()
                            }

                            else -> {

                            }
                        }
                    }
                }
            }

            else -> {
                _allComics = domain.comics().cachedIn(viewModelScope)
            }
        }
    }

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

    fun switchSession(sessionSelected: ComicsSession) {
        viewModelScope.launch {
            if (sessionSelected != _dropDownMenu.value.selectedItem.model) {
                when (sessionSelected) {
                    ComicsSession.FAVORITES -> {
                        domain.favorites { resultOf ->
                            when (resultOf) {
                                is ResultOf.Success -> {
                                    _favorites.update {
                                        it.copy(model = resultOf.value)
                                    }
                                    val options = _dropDownMenu.value.items.map {
                                        if (it.model == sessionSelected) {
                                            it.copy(isSelected = true)
                                        } else {
                                            it.copy(isSelected = false)
                                        }
                                    }
                                    _dropDownMenu.update {
                                        it.copy(
                                            expanded = !it.expanded,
                                            items = options,
                                            selectedItem = it.selectedItem.copy(sessionSelected)
                                        )
                                    }
                                    _allComics = _allComics.cancellable()
                                }

                                else -> {

                                }
                            }
                        }
                    }

                    else -> {
                        _favorites.update {
                            it.copy(model = emptyList())
                        }
                        _allComics = domain.comics().cachedIn(viewModelScope)
                        val options = _dropDownMenu.value.items.map {
                            if (it.model == sessionSelected) {
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = false)
                            }
                        }
                        _dropDownMenu.update {
                            it.copy(
                                expanded = !it.expanded,
                                items = options,
                                selectedItem = it.selectedItem.copy(sessionSelected)
                            )
                        }
                    }
                }

            }
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