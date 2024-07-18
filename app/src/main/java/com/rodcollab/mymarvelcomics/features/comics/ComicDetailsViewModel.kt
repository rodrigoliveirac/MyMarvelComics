package com.rodcollab.mymarvelcomics.features.comics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
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


@HiltViewModel
class ComicDetailsViewModel @Inject constructor(
    private val domain: ComicDomain,
    savedStateHandle: SavedStateHandle,
    private val app: Application,
) : AndroidViewModel(app) {
    private suspend fun addOrRemoveFromFavorites(comicId: Int) {
        domain.addOrRemoveFromFavorites(comicId, _uiState.value.model!!) { resultOf ->
            when (resultOf) {
                is ResultOf.Success -> {
                    _uiState.update { currentState ->
                        val newState = !currentState.model?.isFavorite!!
                        currentState.copy(
                            model = currentState.model.copy(isFavorite = newState),
                            errorMsg = app.getString(resultOf.value)
                        )
                    }
                }

                is ResultOf.Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMsg = resultOf.message
                        )
                    }
                }
            }
        }

    }

    fun checkIfIsFavorite() {
        viewModelScope.launch {
            if(_uiState.value.model?.isFavorite == true) {
                _uiState.update {
                    it.copy(askFirst = app.getString(R.string.will_remove_data_from_favorites))
                }
            } else {
                addOrRemoveFromFavorites(comicId)
            }
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(askFirst = null) }
            addOrRemoveFromFavorites(_uiState.value.model?.id!!)

        }
    }

    fun cancel() {
        viewModelScope.launch {
            _uiState.update { it.copy(askFirst = null) }
        }
    }

    private val comicId: Int = checkNotNull(savedStateHandle["comicId"])

    val characters = domain.characters(comicId).cachedIn(viewModelScope)
    private val _uiState = MutableStateFlow<UiState<Comic>>(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            domain.comic(comicId) { resultOf ->
                when (resultOf) {
                    is ResultOf.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, model = resultOf.value)
                        }
                    }

                    is ResultOf.Failure -> {
                        _uiState.update {
                            it.copy(errorMsg = resultOf.message, isLoading = false)
                        }
                    }
                }
            }
        }
    }

}