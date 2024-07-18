package com.rodcollab.mymarvelcomics.features.comics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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
    domain: ComicDomain,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

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